'use strict';

// Amélioration progressive : la navigation et les POST fonctionnent sans ce fichier.
const search = document.querySelector('[data-article-search]');
if (search) {
    document.querySelector('[data-search-container]').hidden = false;
    const rows = [...document.querySelectorAll('[data-article-row]')];
    const normalize = value => value.normalize('NFD').replace(/[\u0300-\u036f]/g, '').toLocaleLowerCase('fr');
    search.addEventListener('input', () => {
        const query = normalize(search.value.trim());
        let visible = 0;
        rows.forEach(row => {
            row.hidden = !normalize(row.dataset.search).includes(query);
            if (!row.hidden) visible++;
        });
        document.querySelector('[data-no-results]').hidden = visible > 0 || rows.length === 0;
        document.querySelector('[data-search-result]').textContent = `${visible} article(s) affiché(s) sur ${rows.length}`;
    });
}

const mouvementForm = document.querySelector('[data-movement-form]');
if (mouvementForm) {
    mouvementForm.addEventListener('submit', event => {
        // Le second bouton utilise formaction ; ne confirmer que les sorties.
        if (!event.submitter?.hasAttribute('data-sortie')) return;
        const quantity = document.getElementById('quantite').value;
        const label = mouvementForm.dataset.reference;
        if (!window.confirm(`Confirmer la sortie de ${quantity} unité(s) ?\n${label}\nLe stock sera vérifié par le serveur.`)) {
            event.preventDefault();
        }
    });
}
