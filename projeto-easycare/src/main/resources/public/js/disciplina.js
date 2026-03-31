function toggleForm() {
    const form = document.getElementById("formDisciplina");
    const tela = document.getElementById("sobreTela");

    if (form.style.display === "none" || form.style.display === "") {
        form.style.display = "block";
        tela.style.display = "block";
    } else {
        form.style.display = "none";
        tela.style.display = "none";
    }
}

function marcarItem(botao) {
    const item = botao.closest('.item');
    item.classList.toggle('feito');
}

function excluirDisciplina(id) { 
    id = Number(id);
    if (!confirm("Tem certeza que deseja excluir esta disciplina?")) {
        return;
    }

    fetch(`/disciplinas/${id}/excluir`, {
        method: "POST"
    })
    .then(response => {
        if (response.ok) {
            const card = document.getElementById(`card-${id}`);
            if (card) {
                card.style.transition = "opacity 0.3s";
                card.style.opacity = "0";

                setTimeout(() => card.remove(), 300);
            }
        } else {
            alert("Erro ao excluir disciplina.");
        }
    })
    .catch(() => {
        alert("Falha na conexão com o servidor.");
    });
}

function editarDisciplina(id, nome, descricao) {
    const edita = document.getElementById("editarDisciplina");

    // Preenche campos
    document.getElementById("editNome").value = nome;
    document.getElementById("editDescricao").value = descricao;

    // Ajusta destino do form
    document.getElementById("editarForm").action = `/disciplinas/${id}/editar`;

    // Exibe modal
    edita.style.display = "block";
    document.getElementById("sobreTela").style.display = "block";
}

function fecharEditar() {
    document.getElementById("editarDisciplina").style.display = "none";
    document.getElementById("sobreTela").style.display = "none";
}

function fecharQualquer() {
    document.getElementById("editarDisciplina").style.display = "none";
    document.getElementById("formDisciplina").style.display = "none";
    document.getElementById("sobreTela").style.display = "none";
}
