document.addEventListener('DOMContentLoaded', () => {
  const _sd = document.getElementById('serverData');
  const rawStock   = _sd ? (_sd.getAttribute('data-stock')   || '[]') : '[]';
  const rawPopular = _sd ? (_sd.getAttribute('data-popular') || '[]') : '[]';
  const rawChart   = _sd ? (_sd.getAttribute('data-chart')   || '[]') : '[]';

  let serverChartDatasets = [];
  try { serverChartDatasets = JSON.parse(rawChart); } catch(e) { console.error('chart parse err', e); }

  // CHART DATA & OPTIONS
  const CHART_DATA = {
    labels: ['Jan', 'Feb', 'Mar', 'Apr', 'Mei', 'Jun', 'Jul', 'Agt', 'Sep', 'Okt', 'Nov', 'Des'],
    datasets: serverChartDatasets.length > 0 ? serverChartDatasets : [
      { label: 'HP',       data: [12,18,15,22,19,25,28,20,18,24,26,30], borderColor:'rgba(79,142,247,1)',  backgroundColor:'rgba(79,142,247,0.08)',  fill:true, tension:0.4, borderWidth:2, pointRadius:4, pointHoverRadius:6, pointBackgroundColor:'rgba(79,142,247,1)'  },
      { label: 'Laptop',   data: [8,10,9,12,11,14,16,15,13,17,19,21],   borderColor:'rgba(247,196,79,1)',  backgroundColor:'rgba(247,196,79,0.08)',  fill:true, tension:0.4, borderWidth:2, pointRadius:4, pointHoverRadius:6, pointBackgroundColor:'rgba(247,196,79,1)'  },
      { label: 'Tablet',   data: [4,5,6,7,5,8,9,8,6,10,12,14],          borderColor:'rgba(79,199,138,1)',  backgroundColor:'rgba(79,199,138,0.08)',  fill:true, tension:0.4, borderWidth:2, pointRadius:4, pointHoverRadius:6, pointBackgroundColor:'rgba(79,199,138,1)'  },
      { label: 'Blender',  data: [6,7,5,9,8,10,11,10,8,12,14,16],       borderColor:'rgba(247,101,79,1)',  backgroundColor:'rgba(247,101,79,0.08)',  fill:true, tension:0.4, borderWidth:2, pointRadius:4, pointHoverRadius:6, pointBackgroundColor:'rgba(247,101,79,1)'  },
      { label: 'Kulkas',   data: [3,4,5,4,6,5,7,6,5,8,10,12],           borderColor:'rgba(160,120,255,1)', backgroundColor:'rgba(160,120,255,0.08)', fill:true, tension:0.4, borderWidth:2, pointRadius:4, pointHoverRadius:6, pointBackgroundColor:'rgba(160,120,255,1)' },
      { label: 'AC',       data: [5,6,7,8,9,10,11,12,13,14,15,16],      borderColor:'rgba(79,220,220,1)',  backgroundColor:'rgba(79,220,220,0.08)',  fill:true, tension:0.4, borderWidth:2, pointRadius:4, pointHoverRadius:6, pointBackgroundColor:'rgba(79,220,220,1)'  },
      { label: 'TV',       data: [10,12,14,16,18,20,22,24,26,28,30,32], borderColor:'rgba(247,160,79,1)',  backgroundColor:'rgba(247,160,79,0.08)',  fill:true, tension:0.4, borderWidth:2, pointRadius:4, pointHoverRadius:6, pointBackgroundColor:'rgba(247,160,79,1)'  },
      { label: 'Headphone',data: [7,8,9,10,11,12,13,14,15,16,17,18],    borderColor:'rgba(79,199,138,1)',  backgroundColor:'rgba(79,199,138,0.08)',  fill:true, tension:0.4, borderWidth:2, pointRadius:4, pointHoverRadius:6, pointBackgroundColor:'rgba(79,199,138,1)'  }
    ]
  };

  const CHART_OPTIONS = {
    responsive: true,
    maintainAspectRatio: false,
    interaction: { mode: 'index', intersect: false },
    plugins: {
      legend: { labels: { color:'#7b82a0', font:{ family:'DM Sans', size:12 }, usePointStyle:true, pointStyle:'circle' } }
    },
    scales: {
      x: { ticks:{ color:'#7b82a0' }, grid:{ color:'rgba(42,47,69,0.4)' } },
      y: { ticks:{ color:'#7b82a0', callback: v => 'Rp '+v+'jt' }, grid:{ color:'rgba(42,47,69,0.4)' }, beginAtZero:true }
    }
  };

  // CHART BUILD
  let salesChartInstance = null;

  function buildChart() {
    const canvas = document.getElementById('salesChart');
    if (!canvas) return;
    const wrap = canvas.parentElement;
    if (!wrap || wrap.offsetWidth === 0) return;
    if (salesChartInstance) {
      salesChartInstance.data = CHART_DATA;
      salesChartInstance.update();
      salesChartInstance.resize();
      return;
    }
    canvas.width  = wrap.offsetWidth;
    canvas.height = 260;
    salesChartInstance = new Chart(canvas.getContext('2d'), {
      type: 'line', data: CHART_DATA, options: CHART_OPTIONS
    });
  }

  // ResizeObserver untuk chart-wrap
  const chartWrap = document.querySelector('.chart-wrap');
  if (chartWrap && typeof ResizeObserver !== 'undefined') {
    const ro = new ResizeObserver(entries => {
      for (const entry of entries) {
        if (entry.contentRect.width > 0) {
          buildChart();
          if (salesChartInstance) ro.disconnect();
        }
      }
    });
    ro.observe(chartWrap);
  }

  // NAVIGASI SIDEBAR
  const labels     = { dashboard:'Dashboard', users:'Manajemen User', table:'Tabel Produk', Pengiriman:'Pengiriman Barang', complaints:'Manajemen Pengaduan' };
  const navItems   = document.querySelectorAll('.nav-item');
  const pages      = document.querySelectorAll('.page');
  const headerTitle= document.getElementById('header-title');
  let   openDropId = null;

  function closeDropdown() {
    if (openDropId !== null) {
      document.getElementById('drop-' + openDropId)?.classList.remove('show');
      document.querySelector(`.action-btn[data-pid="${openDropId}"]`)?.classList.remove('is-open');
      openDropId = null;
    }
  }

  function switchPage(pg) {
    localStorage.setItem('activeAdminPage', pg);
    navItems.forEach(i => i.classList.remove('active'));
    pages.forEach(p => p.classList.remove('active'));
    const targetNav  = document.querySelector(`.nav-item[data-page="${pg}"]`);
    const targetPage = document.getElementById('page-' + pg);
    if (targetNav)  targetNav.classList.add('active');
    if (targetPage) targetPage.classList.add('active');
    if (headerTitle) headerTitle.textContent = labels[pg] || pg;
    closeDropdown();
    if (pg === 'dashboard') {
      requestAnimationFrame(() => requestAnimationFrame(buildChart));
    }
  }

  navItems.forEach(item => item.addEventListener('click', () => switchPage(item.dataset.page)));

  document.getElementById('toggle-btn').addEventListener('click', () => {
    document.body.classList.toggle('sidebar-closed');
    if (salesChartInstance) setTimeout(() => salesChartInstance.resize(), 350);
  });

  // Initial page
  const initialPage = (function () {
    const flashTarget = document.getElementById('flashPageTarget')?.value;
    if (flashTarget) return flashTarget;
    return localStorage.getItem('activeAdminPage') || 'dashboard';
  })();
  switchPage(initialPage);
  if (initialPage !== 'dashboard') {
    requestAnimationFrame(() => requestAnimationFrame(buildChart));
  }

  // STOCK LIST
  let stockData = [];
  try { stockData = JSON.parse(rawStock); } catch(e) { console.error('stock parse err', e); }
  if (!stockData || stockData.length === 0) stockData = [{ label:'HP', count:0, color:'#4f8ef7' }];

  const maxStock  = Math.max(...stockData.map(s => s.count));
  const stockListEl = document.getElementById('stockList');
  if (stockListEl) {
    stockData.forEach(s => {
      const pct = maxStock > 0 ? Math.round(s.count / maxStock * 100) : 0;
      stockListEl.innerHTML += `<div class="stock-item">
        <div class="stock-dot" style="background:${s.color}"></div>
        <div class="stock-name">${s.label}</div>
        <div class="stock-bar-wrap"><div class="stock-bar" style="width:${pct}%;background:${s.color}"></div></div>
        <div class="stock-num">${s.count}</div>
      </div>`;
    });
  }

  // POPULAR TABLE
  let popularData = [];
  try { popularData = JSON.parse(rawPopular); } catch(e) { console.error('popular parse err', e); }
  if (!popularData || popularData.length === 0) popularData = [{ nama:'Belum ada data', kat:'-', terjual:0, harga:0, stok:0 }];

  function katBadge(k) {
    const m = { Hp:'badge-hp', Laptop:'badge-laptop', Tablet:'badge-tablet', Blender:'badge-blender', Kulkas:'badge-kulkas', Tv:'badge-tv', Ac:'badge-ac', Headphone:'badge-hp' };
    return `<span class="badge ${m[k]||'badge-hp'}">${k}</span>`;
  }
  function stokBadge(s) {
    if (s === 0) return `<span class="stock-badge stock-out">● Habis</span>`;
    if (s < 10)  return `<span class="stock-badge stock-low">● Rendah</span>`;
    return `<span class="stock-badge stock-ok">● Tersedia</span>`;
  }

  const popTbody = document.getElementById('popularTable');
  if (popTbody) {
    popularData.forEach((p, i) => {
      const rc = i===0?'gold': i===1?'silver': i===2?'bronze':'';
      popTbody.innerHTML += `<tr>
        <td><span class="rank-num ${rc}">${i+1}</span></td>
        <td><strong>${p.nama}</strong></td>
        <td>${katBadge(p.kat)}</td>
        <td><strong>${p.terjual}</strong> unit</td>
        <td class="price-cell">Rp ${(p.terjual * p.harga / 1e6).toFixed(1)}jt</td>
        <td>${stokBadge(p.stok)} <span style="color:var(--muted);font-size:12px;margin-left:4px">${p.stok}</span></td>
      </tr>`;
    });
  }

  // SEARCH & FILTER
  const searchInput = document.getElementById('searchInput');
  if (searchInput) {
    searchInput.addEventListener('input', e => {
      const q = e.target.value.toLowerCase().trim();
      document.querySelectorAll('#productTable tr').forEach(row => {
        const cell = row.querySelectorAll('td')[2];
        const txt  = cell ? cell.textContent.toLowerCase() : '';
        row.style.display = (q === '' || txt.includes(q)) ? '' : 'none';
      });
    });
  }

  const userSearchInput = document.getElementById('userSearchInput');
  if (userSearchInput) {
    userSearchInput.addEventListener('input', e => {
      const q = e.target.value.toLowerCase().trim();
      document.querySelectorAll('.user-table tbody tr').forEach(row => {
        const u = row.querySelectorAll('td')[1]?.textContent.toLowerCase() || '';
        const m = row.querySelectorAll('td')[2]?.textContent.toLowerCase() || '';
        row.style.display = (q==='' || u.includes(q) || m.includes(q)) ? '' : 'none';
      });
    });
  }

  const complaintSearchInput = document.getElementById('complaintSearchInput');
  if (complaintSearchInput) {
    complaintSearchInput.addEventListener('input', e => {
      const q = e.target.value.toLowerCase().trim();
      document.querySelectorAll('#complaintsTable tbody tr').forEach(row => {
        const a = row.querySelectorAll('td')[1]?.textContent.toLowerCase() || '';
        const b = row.querySelectorAll('td')[2]?.textContent.toLowerCase() || '';
        const c = row.querySelectorAll('td')[4]?.textContent.toLowerCase() || '';
        row.style.display = (q==='' || a.includes(q) || b.includes(q) || c.includes(q)) ? '' : 'none';
      });
    });
  }

  const filterGroup = document.getElementById('filterGroup');
  if (filterGroup) {
    filterGroup.addEventListener('click', e => {
      const btn = e.target.closest('.filter-btn');
      if (!btn) return;
      document.querySelectorAll('.filter-btn').forEach(b => b.classList.remove('active'));
      btn.classList.add('active');
      const cat = btn.dataset.cat;
      document.querySelectorAll('#productTable tr').forEach(row => {
        const rowCat = row.dataset.kategori || '';
        row.style.display = (cat==='all' || rowCat===cat) ? '' : 'none';
      });
    });
  }

  // DROPDOWN AKSI PRODUK
  const productTable = document.getElementById('productTable');
  if (productTable) {
    productTable.addEventListener('click', e => {
      const btn = e.target.closest('.action-btn');
      if (btn) {
        e.stopPropagation();
        const pid  = btn.dataset.pid;
        const drop = document.getElementById('drop-' + pid);
        if (!drop) return;
        if (openDropId === pid) { closeDropdown(); return; }
        closeDropdown();
        const rect = btn.getBoundingClientRect();
        if (window.innerHeight - rect.bottom < 200) drop.classList.add('drop-up');
        else drop.classList.remove('drop-up');
        drop.classList.add('show');
        btn.classList.add('is-open');
        openDropId = pid;
        return;
      }
      const item = e.target.closest('.drop-item');
      if (item) {
        e.stopPropagation();
        closeDropdown();
        const action = item.dataset.action;
        if (action === 'edit') {
          openEditProduk(item);
        } else {
          openModal(action, item.dataset.pid, item.dataset.nama, parseInt(item.dataset.stok) || 0);
        }
      }
    });
  }
  document.addEventListener('click', closeDropdown);

  // MODAL STOK / HAPUS
  const overlay    = document.getElementById('modalOverlay');
  const mIconWrap  = document.getElementById('mIconWrap');
  const mIconSvg   = document.getElementById('mIconSvg');
  const mTitle     = document.getElementById('mTitle');
  const mSubtitle  = document.getElementById('mSubtitle');
  const mProduct   = document.getElementById('mProduct');
  const mStokRow   = document.getElementById('mStokRow');
  const mStokNow   = document.getElementById('mStokNow');
  const mStokAfter = document.getElementById('mStokAfter');
  const mQtySection= document.getElementById('mQtySection');
  const mQtyLabel  = document.getElementById('mQtyLabel');
  const qtyInput   = document.getElementById('qtyInput');
  const mBtnOk     = document.getElementById('mBtnOk');
  let activePid = null, activeAction = null, activeStok = 0, activeNama = '';

  const ICON_PATHS = {
    add:    '<line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/>',
    reduce: '<line x1="5" y1="12" x2="19" y2="12"/>',
    delete: '<polyline points="3 6 5 6 21 6"/><path d="M19 6l-1 14a2 2 0 0 1-2 2H8a2 2 0 0 1-2-2L5 6"/><path d="M10 11v6M14 11v6"/><path d="M9 6V4h6v2"/>'
  };

  function updateAfterPreview() {
    if (activeAction === 'delete') return;
    const qty   = Math.max(1, parseInt(qtyInput.value) || 1);
    const after = activeAction === 'add' ? activeStok + qty : Math.max(0, activeStok - qty);
    mStokAfter.textContent = after;
    mStokAfter.className   = 'msb-val ' + (after===0?'red': after<10?'yellow':'green');
  }

  function openModal(action, pid, nama, stok) {
    activePid = pid; activeAction = action; activeStok = isNaN(stok)?0:stok; activeNama = nama;
    mIconWrap.className  = 'modal-icon-wrap icon-' + action;
    mIconSvg.innerHTML   = ICON_PATHS[action] || '';
    mProduct.textContent = nama;
    if (action === 'add') {
      mTitle.textContent = 'Tambah Stok'; mSubtitle.textContent = 'Masukkan jumlah unit yang ingin ditambahkan.';
      mQtyLabel.textContent = 'JUMLAH TAMBAHAN'; mBtnOk.className = 'btn-ok add'; mBtnOk.textContent = 'Tambah Stok';
      mStokRow.style.display = mQtySection.style.display = '';
      mStokNow.textContent = activeStok; mStokNow.className = 'msb-val'; qtyInput.value = 1; updateAfterPreview();
    } else if (action === 'reduce') {
      mTitle.textContent = 'Kurangi Stok'; mSubtitle.textContent = 'Masukkan jumlah unit yang ingin dikurangi.';
      mQtyLabel.textContent = 'JUMLAH PENGURANGAN'; mBtnOk.className = 'btn-ok reduce'; mBtnOk.textContent = 'Kurangi Stok';
      mStokRow.style.display = mQtySection.style.display = '';
      mStokNow.textContent = activeStok; mStokNow.className = 'msb-val'; qtyInput.value = 1; updateAfterPreview();
    } else {
      mTitle.textContent = 'Hapus Produk?'; mSubtitle.textContent = 'Produk akan dihapus permanen. Tidak bisa dibatalkan.';
      mBtnOk.className = 'btn-ok delete'; mBtnOk.textContent = 'Ya, Hapus Produk';
      mStokRow.style.display = mQtySection.style.display = 'none';
    }
    overlay.classList.add('show');
  }

  function closeModal() { overlay.classList.remove('show'); activePid = activeAction = null; }

  document.getElementById('qtyMinus').addEventListener('click', () => {
    qtyInput.value = Math.max(1, parseInt(qtyInput.value)-1); updateAfterPreview();
  });
  document.getElementById('qtyPlus').addEventListener('click', () => {
    const max = activeAction==='reduce' ? activeStok : 9999;
    qtyInput.value = Math.min(max, parseInt(qtyInput.value)+1); updateAfterPreview();
  });
  qtyInput.addEventListener('input', updateAfterPreview);
  document.getElementById('mBtnCancel').addEventListener('click', closeModal);
  overlay.addEventListener('click', e => { if (e.target===overlay) closeModal(); });

  mBtnOk.addEventListener('click', () => {
    if (!activePid) return;
    const qty = Math.max(1, parseInt(qtyInput.value)||1);
    if (activeAction==='add') {
      submitForm('/admin/tambahStok', { id:activePid, jumlah:qty });
    } else if (activeAction==='reduce') {
      if (qty > activeStok) { showToast('Stok tidak mencukupi!','#f7654f'); closeModal(); return; }
      submitForm('/admin/kurangiStok', { id:activePid, jumlah:qty });
    } else {
      submitForm('/admin/hapus/'+activePid, {}, 'GET');
    }
    closeModal();
  });

  // MODAL TAMBAH / EDIT PRODUK
  const produkOverlay = document.getElementById('produkModalOverlay');
  const produkForm    = document.getElementById('produkForm');
  const produkTitle   = document.getElementById('produkModalTitle');
  const produkFormId  = document.getElementById('produkFormId');

  const btnTambahProduk = document.getElementById('btnTambahProduk');
  if (btnTambahProduk) {
    btnTambahProduk.addEventListener('click', () => {
      produkTitle.textContent = 'Tambah Produk';
      produkForm.action = '/admin/tambah';
      produkFormId.value = '';
      produkForm.reset();
      produkOverlay.classList.add('show');
    });
  }

  function openEditProduk(item) {
    produkTitle.textContent = 'Edit Produk';
    produkForm.action = '/admin/edit';
    produkFormId.value = item.dataset.pid;
    document.getElementById('pNama').value       = item.dataset.nama       || '';
    document.getElementById('pMerk').value       = item.dataset.merk       || '';
    document.getElementById('pHarga').value      = item.dataset.harga      || '';
    document.getElementById('pStok').value       = item.dataset.stok       || '0';
    document.getElementById('pImageUrl').value   = item.dataset.imageurl  !== 'null' ? (item.dataset.imageurl  || '') : '';
    document.getElementById('pDeskripsi').value  = item.dataset.deskripsi !== 'null' ? (item.dataset.deskripsi || '') : '';
    document.getElementById('pSpesifikasi').value= item.dataset.spesifikasi!=='null' ? (item.dataset.spesifikasi||'') : '';
    const sel = document.getElementById('pKategori');
    for (let opt of sel.options) {
      if (opt.value === item.dataset.kategori) { opt.selected = true; break; }
    }
    produkOverlay.classList.add('show');
  }

  function closeProdukModal() { produkOverlay.classList.remove('show'); }
  document.getElementById('closeProdukModal').addEventListener('click', closeProdukModal);
  document.getElementById('closeProdukModal2').addEventListener('click', closeProdukModal);
  produkOverlay.addEventListener('click', e => { if (e.target===produkOverlay) closeProdukModal(); });

  // PASSWORD TOGGLE
  document.querySelectorAll('.pw-toggle-btn').forEach(btn => {
    btn.addEventListener('click', () => {
      const targetId = btn.dataset.target;
      const input = targetId
        ? document.getElementById(targetId)
        : btn.closest('.pw-input-wrap')?.querySelector('input');
      if (!input) return;
      const svg = btn.querySelector('svg');
      const isHidden = input.type === 'password';
      input.type = isHidden ? 'text' : 'password';
      if (svg) {
        svg.innerHTML = isHidden
          ? '<path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/>'
          : '<path d="M2 8c4 4 16 4 20 0" /><path d="M12 11v4" /><path d="M8 9.5l-2 3" /><path d="M16 9.5l2 3" />';
      }
      btn.style.opacity = isHidden ? '1' : '0.5';
    });
  });

  // MODAL TAMBAH / EDIT USER
  const userOverlay = document.getElementById('userModalOverlay');
  const userForm    = document.getElementById('userForm');
  const userTitle   = document.getElementById('userModalTitle');
  const userFormId  = document.getElementById('userFormId');

  const btnTambahUser = document.getElementById('btnTambahUser');
  if (btnTambahUser) {
    btnTambahUser.addEventListener('click', () => {
      userTitle.textContent = 'Tambah User';
      userForm.action = '/admin/user/tambah';
      userFormId.value = '';
      userForm.reset();
      document.getElementById('uPasswordField').style.display = '';
      document.getElementById('uPassword').required = true;
      userOverlay.classList.add('show');
    });
  }

  window.openEditUser = function (btn) {
    userTitle.textContent = 'Edit User';
    userForm.action = '/admin/user/edit';
    userFormId.value = btn.dataset.id;
    document.getElementById('uUsername').value = btn.dataset.username || '';
    document.getElementById('uEmail').value    = btn.dataset.email    || '';
    document.getElementById('uRole').value     = btn.dataset.role     || 'USER';
    document.getElementById('uPasswordField').style.display = 'none';
    document.getElementById('uPassword').required = false;
    userOverlay.classList.add('show');
  };

  function closeUserModal() { userOverlay.classList.remove('show'); }
  document.getElementById('closeUserModal').addEventListener('click', closeUserModal);
  document.getElementById('closeUserModal2').addEventListener('click', closeUserModal);
  userOverlay.addEventListener('click', e => { if (e.target===userOverlay) closeUserModal(); });

  // HELPER SUBMIT FORM
  function submitForm(action, params, method='POST') {
    const form = document.createElement('form');
    form.method = method==='GET' ? 'GET' : 'POST';
    form.action = action;
    const csrfMeta = document.querySelector('meta[name="_csrf"]');
    if (csrfMeta && method!=='GET') {
      const csrf = document.createElement('input');
      csrf.type='hidden'; csrf.name='_csrf'; csrf.value=csrfMeta.content;
      form.appendChild(csrf);
    }
    Object.entries(params).forEach(([k,v]) => {
      const inp = document.createElement('input');
      inp.type='hidden'; inp.name=k; inp.value=v;
      form.appendChild(inp);
    });
    document.body.appendChild(form);
    form.submit();
  }

  // TOAST
  let toastTimer = null;
  function showToast(msg, color='#4fc78a') {
    const toast = document.getElementById('toast');
    if (!toast) return;
    document.getElementById('tDot').style.background = color;
    document.getElementById('tMsg').textContent = msg;
    toast.classList.add('show');
    clearTimeout(toastTimer);
    toastTimer = setTimeout(() => toast.classList.remove('show'), 3000);
  }

}); // end DOMContentLoaded

// PROFILE PANEL (IIFE terpisah)
(function () {
  const wrapper = document.getElementById('profileWrapper');
  const btn     = document.getElementById('profileBtn');
  if (!wrapper || !btn) return;

  btn.addEventListener('click', e => { e.stopPropagation(); wrapper.classList.toggle('open'); });
  document.addEventListener('click', e => { if (!wrapper.contains(e.target)) wrapper.classList.remove('open'); });
  document.addEventListener('keydown', e => { if (e.key==='Escape') wrapper.classList.remove('open'); });

  const pwOverlay = document.getElementById('pwModalOverlay');
  const btnOpenPw = document.getElementById('btnOpenPw');
  const btnClose1 = document.getElementById('btnClosePw');
  const btnClose2 = document.getElementById('btnClosePw2');

  function openPwModal()  { wrapper.classList.remove('open'); if (pwOverlay) pwOverlay.classList.add('active'); }
  function closePwModal() { if (pwOverlay) pwOverlay.classList.remove('active'); }

  if (btnOpenPw) btnOpenPw.addEventListener('click', openPwModal);
  if (btnClose1) btnClose1.addEventListener('click', closePwModal);
  if (btnClose2) btnClose2.addEventListener('click', closePwModal);
  if (pwOverlay) pwOverlay.addEventListener('click', e => { if (e.target===pwOverlay) closePwModal(); });

  const hasFlash = pwOverlay && (pwOverlay.querySelector('.pw-alert-error') || pwOverlay.querySelector('.pw-alert-success'));
  if (hasFlash) openPwModal();

  // SEARCH PENGIRIMAN
  const orderSearchInput = document.getElementById('orderSearchInput');
  if (orderSearchInput) {
    orderSearchInput.addEventListener('input', function () {
      const q = this.value.toLowerCase();
      document.querySelectorAll('#ordersTable tbody tr').forEach(row => {
        row.style.display = row.textContent.toLowerCase().includes(q) ? '' : 'none';
      });
    });
  }
})();
