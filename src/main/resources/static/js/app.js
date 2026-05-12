function toggleEdit(id) {
    const row   = document.getElementById('row-'  + id);
    const edit  = document.getElementById('edit-' + id);
    const shown = edit.style.display === 'flex';
    row.style.display  = shown ? 'flex'  : 'none';
    edit.style.display = shown ? 'none'  : 'flex';
}
