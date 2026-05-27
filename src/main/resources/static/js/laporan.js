document.addEventListener('DOMContentLoaded', () => {
    const input = document.getElementById('productSearchInput');
    const hiddenId = document.getElementById('productId');
    const dropdown = document.getElementById('productDropdown');

    if (!input || !dropdown) return;

    const items = dropdown.querySelectorAll('.autocomplete-item');

    input.addEventListener('focus', () => {
        dropdown.classList.add('show');
    });

    input.addEventListener('input', () => {
        const filter = input.value.toLowerCase();
        dropdown.classList.add('show');
        items.forEach(item => {
            if (item.textContent.toLowerCase().includes(filter)) {
                item.style.display = 'block';
            } else {
                item.style.display = 'none';
            }
        });
    });

    items.forEach(item => {
        item.addEventListener('click', () => {
            input.value = item.dataset.value === "0" ? "" : item.textContent;
            hiddenId.value = item.dataset.value;
            dropdown.classList.remove('show');
        });
    });

    document.addEventListener('click', (e) => {
        if (!input.contains(e.target) && !dropdown.contains(e.target)) {
            dropdown.classList.remove('show');
        }
    });
});
