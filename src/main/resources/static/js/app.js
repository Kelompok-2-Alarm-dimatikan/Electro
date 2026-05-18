function toggleEdit(id) {
    const row   = document.getElementById('row-'  + id);
    const edit  = document.getElementById('edit-' + id);
    const shown = edit.style.display === 'flex';
    row.style.display  = shown ? 'flex'  : 'none';
    edit.style.display = shown ? 'none'  : 'flex';
}
function toggleProfile() {
    document.getElementById('profileWrap').classList.toggle('open');
    }
    document.addEventListener('click', function(e) {
        const wrap = document.getElementById('profileWrap');
        if (wrap && !wrap.contains(e.target)) {
            wrap.classList.remove('open');
        }
    });
