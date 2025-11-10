const API = "http://localhost:8082";
let token = localStorage.getItem("token") || null;

// LOGIN
async function login() {
    const email = document.getElementById("email").value;
    const senha = document.getElementById("senha").value;

    const res = await fetch(`${API}/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, senha })
    });

    const result = await res.json();
    console.log("RESPOSTA LOGIN:", result);

    if (!res.ok) {
        alert(result.erro || "Erro no login");
        return;
    }

    token = result.token || result.sessionId;
    console.log("TOKEN SALVO:", token);
    localStorage.setItem("token", token);

    document.getElementById("login").style.display = "none";
    document.getElementById("app").style.display = "block";
    loadTasks();
}

// LOGOUT
async function logout() {
    await fetch(`${API}/auth/logout`, {
        method: "POST",
        headers: { "X-Auth-Token": token }
    });

    token = null;
    localStorage.removeItem("token");
    document.getElementById("login").style.display = "block";
    document.getElementById("app").style.display = "none";
}

// CARREGAR TAREFAS
async function loadTasks() {
    try {
        console.log("TOKEN ENVIADO:", token);

        const res = await fetch(`${API}/api/tarefas`, {
            method: "GET",
            headers: {
                "Accept": "application/json",
                "X-Auth-Token": token
            }
        });

        console.log("RESPOSTA STATUS:", res.status);
        if (!res.ok) {
            const text = await res.text();
            console.error("RESPOSTA DO BACKEND:", text);
            return;
        }

        const data = await res.json();
        const tarefas = Array.isArray(data) ? data : data.content;

        const ul = document.getElementById("lista");
        ul.innerHTML = "";

        tarefas.forEach(t => {
            const li = document.createElement("li");
            li.innerHTML = `
                <div class="task-info">
                    <strong>${t.titulo}</strong> - <span class="status">${t.status}</span> - <span class="priority">${t.prioridade}</span>
                    <div class="description">${t.descricao || 'Sem descrição'}</div>
                </div>
                <div class="task-buttons">
                    <button onclick="editTask('${t.idTarefa}')">Editar</button>
                    <button onclick="deleteTask('${t.idTarefa}')" class="delete">Excluir</button>
                </div>
            `;
            ul.appendChild(li);
        });

    } catch (error) {
        console.error("Erro ao carregar tarefas:", error);
    }
}

// CRIAR TAREFA
async function createTask() {
    const titulo = document.getElementById("titulo").value;
    const descricao = document.getElementById("descricao").value;
    const prioridade = document.getElementById("prioridade").value;

    await fetch(`${API}/api/tarefas`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "X-Auth-Token": token
        },
        body: JSON.stringify({ titulo, descricao, prioridade })
    });

    document.getElementById("titulo").value = "";
    document.getElementById("descricao").value = "";
    loadTasks();
}

// EDITAR TAREFA
async function editTask(id) {
    const novoTitulo = prompt("Novo título:");
    const novaDescricao = prompt("Nova descrição:");
    const novaPrioridade = prompt("Prioridade (BAIXA, MEDIA, ALTA):", "MEDIA");
    const novoStatus = prompt("Status (PENDENTE, EM_ANDAMENTO, CONCLUIDA):", "PENDENTE");

    if (!novoTitulo || !novaDescricao) return;

    await fetch(`${API}/api/tarefas/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "X-Auth-Token": token
        },
        body: JSON.stringify({
            titulo: novoTitulo,
            descricao: novaDescricao,
            prioridade: novaPrioridade,
            status: novoStatus
        })
    });

    loadTasks();
}

// DELETAR TAREFA
async function deleteTask(id) {
    if (!confirm("Deseja realmente excluir esta tarefa?")) return;

    await fetch(`${API}/api/tarefas/${id}`, {
        method: "DELETE",
        headers: { "X-Auth-Token": token }
    });

    loadTasks();
}

// mantém sessão ativa
if (token) {
    document.getElementById("login").style.display = "none";
    document.getElementById("app").style.display = "block";
    loadTasks();
}
