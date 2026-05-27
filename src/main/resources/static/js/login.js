function togglePassword(inputId, btn) {
            const input = document.getElementById(inputId);
            const svg = btn.querySelector('svg');
            if (input.type === 'password') {
                input.type = 'text';
                if (svg) svg.innerHTML = '<path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/>';
                btn.style.opacity = '1';
            } else {
                input.type = 'password';
                if (svg) svg.innerHTML = '<path d="M2 8c4 4 16 4 20 0" /><path d="M12 11v4" /><path d="M8 9.5l-2 3" /><path d="M16 9.5l2 3" />';
                btn.style.opacity = '0.5';
            }
        }