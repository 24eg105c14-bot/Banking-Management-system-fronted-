const API = 'http://localhost:8080/api';
async function request(path, options={}) {
  const response = await fetch(API + path, {headers:{'Content-Type':'application/json',...(options.headers||{})}, ...options});
  const raw = await response.text();
  let data; try { data = raw ? JSON.parse(raw) : {}; } catch { data = {message:raw}; }
  if (!response.ok) throw new Error(data.message || data.error || `Request failed (${response.status})`);
  return data;
}
function message(text){const el=document.getElementById('message');if(el)el.textContent=text;}
const reg=document.getElementById('registerForm');
if(reg) reg.addEventListener('submit',async e=>{e.preventDefault();try{const d=await request('/customers/register',{method:'POST',body:JSON.stringify({name:document.getElementById('name').value,email:document.getElementById('email').value,password:document.getElementById('password').value})});localStorage.setItem('customerId',d.id);message('Registration successful. You can now log in.');setTimeout(()=>location.href='login.html',900);}catch(err){message(err.message);}});
const login=document.getElementById('loginForm');
if(login) login.addEventListener('submit',async e=>{e.preventDefault();try{const d=await request('/customers/login',{method:'POST',body:JSON.stringify({email:document.getElementById('email').value,password:document.getElementById('password').value})});localStorage.setItem('customerId',d.id);message('Login successful.');setTimeout(()=>location.href='dashboard.html',500);}catch(err){message(err.message);}});
async function loadDashboard(){const info=document.getElementById('accountInfo');if(!info)return;const id=localStorage.getItem('customerId');if(!id){info.textContent='Please log in first.';return;}try{const d=await request('/customers/'+id);info.innerHTML=`<strong>${escapeHtml(d.name)}</strong><br>Email: ${escapeHtml(d.email)}<br>Account number: ${escapeHtml(d.accountNumber)}<br>Balance: ₹${Number(d.balance).toFixed(2)}`;}catch(e){info.textContent=e.message;}}
async function makeTransaction(type){const id=localStorage.getItem('customerId'), amount=Number(document.getElementById('amount').value);if(!id){message('Please log in first.');return;}if(!Number.isFinite(amount)||amount<=0){message('Enter a valid amount.');return;}try{await request('/transactions/'+type,{method:'POST',body:JSON.stringify({customerId:Number(id),amount})});message(`${type==='deposit'?'Deposit':'Withdrawal'} completed (demo).`);document.getElementById('amount').value='';await loadDashboard();}catch(e){message(e.message);}}
async function loadTransactions(){const out=document.getElementById('transactions');if(!out)return;const id=localStorage.getItem('customerId');if(!id){out.textContent='Please log in first.';return;}try{const rows=await request('/transactions/customer/'+id);if(!rows.length){out.textContent='No transactions yet.';return;}out.innerHTML='<table><thead><tr><th>Type</th><th>Amount</th><th>Date</th></tr></thead><tbody>'+rows.map(t=>`<tr><td>${escapeHtml(t.type)}</td><td>₹${Number(t.amount).toFixed(2)}</td><td>${escapeHtml(t.createdAt||'')}</td></tr>`).join('')+'</tbody></table>';}catch(e){message(e.message);}}
function escapeHtml(s){return String(s??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));}
if(document.getElementById('accountInfo'))loadDashboard();
if(document.getElementById('transactions'))loadTransactions();
