// Tab switching logic
        function switchTab(tabId) {
            document.querySelectorAll('.sidebar-menu .menu-item').forEach(item => {
                item.classList.remove('active');
            });
            document.querySelectorAll('.settings-content-area .tab-panel').forEach(panel => {
                panel.classList.remove('active');
            });
            
            // Add active class to clicked menu item
            let targetMenu = Array.from(document.querySelectorAll('.sidebar-menu .menu-item')).find(item => {
                return item.getAttribute('onclick').includes(`'${tabId}'`);
            });
            if (targetMenu) targetMenu.classList.add('active');
            
            // Show target panel
            let targetPanel = document.getElementById(`tab-${tabId}`);
            if (targetPanel) targetPanel.classList.add('active');
            
            // Save tab state in localStorage
            localStorage.setItem('active_settings_tab', tabId);
        }
        
        // Restore tab state on page load
        document.addEventListener('DOMContentLoaded', () => {
            const activeTab = localStorage.getItem('active_settings_tab');
            if (activeTab && ['profile', 'password', 'account'].includes(activeTab)) {
                switchTab(activeTab);
            }
        });

        // Modal triggers
        function openDeleteModal() {
            const modal = document.getElementById('delete-modal');
            modal.style.display = 'flex';
            setTimeout(() => modal.classList.add('show'), 10);
        }

        function closeDeleteModal() {
            const modal = document.getElementById('delete-modal');
            modal.classList.remove('show');
            setTimeout(() => modal.style.display = 'none', 300);
        }

        // Toggle password visibility
        function togglePassword(inputId, button) {
            const input = document.getElementById(inputId);
            if (!input) return;
            
            if (input.type === 'password') {
                input.type = 'text';
                button.classList.add('visible');
            } else {
                input.type = 'password';
                button.classList.remove('visible');
            }
        }

        // Avatar base64 parser and canvas compression
        function handleAvatarUpload(event) {
            const file = event.target.files[0];
            if (!file) return;

            if (file.size > 1024 * 1024) {
                alert("File terlalu besar! Maksimal ukuran file adalah 1MB.");
                return;
            }

            const reader = new FileReader();
            reader.onload = function(e) {
                const img = new Image();
                img.onload = function() {
                    const maxDim = 250;
                    let width = img.width;
                    let height = img.height;
                    
                    if (width > maxDim || height > maxDim) {
                        if (width > height) {
                            height = Math.round((height * maxDim) / width);
                            width = maxDim;
                        } else {
                            width = Math.round((width * maxDim) / height);
                            height = maxDim;
                        }
                    }

                    const canvas = document.createElement('canvas');
                    canvas.width = width;
                    canvas.height = height;
                    const ctx = canvas.getContext('2d');
                    ctx.drawImage(img, 0, 0, width, height);

                    const compressedBase64 = canvas.toDataURL('image/jpeg', 0.85);

                    // Update UI previews
                    const previewImg = document.getElementById('avatar-preview');
                    const textFallback = document.getElementById('avatar-text-fallback');
                    
                    previewImg.src = compressedBase64;
                    previewImg.style.display = 'block';
                    textFallback.style.display = 'none';

                    // Update inputs
                    document.getElementById('avatar-base64-input').value = compressedBase64;
                };
                img.src = e.target.result;
            };
            reader.readAsDataURL(file);
        }