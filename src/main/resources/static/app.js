// State management
let currentUser = null;
let selectedSpecialist = null;
let selectedSlot = null;
let selectedBooking = null;

// Demo credentials
const demoCredentials = {
    CUSTOMER: { username: 'customer1', password: 'password123' },
    SPECIALIST: { username: 'specialist1', password: 'password123' },
    MANAGER: { username: 'manager1', password: 'password123' }
};

// Utility: Escape HTML to prevent XSS
function escapeHtml(value) {
    if (value === null || value === undefined) return '';
    return String(value)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;');
}

// Initialize app when DOM is ready
function initializeApp() {
    // DOM elements
    const demoSection = document.getElementById('demo-section');
    const userPanel = document.getElementById('user-panel');
    const customerSection = document.getElementById('customer-section');
    const specialistSection = document.getElementById('specialist-section');
    const managerSection = document.getElementById('manager-section');
    
    // Login form handler
    const loginForm = document.getElementById('login-form');
    if (loginForm) {
        loginForm.addEventListener('submit', handleLogin);
    }
    
    // Register form handler
    const registerForm = document.getElementById('register-form');
    if (registerForm) {
        registerForm.addEventListener('submit', handleRegister);
    }
    
    // Booking form handler
    const bookingForm = document.getElementById('booking-form');
    if (bookingForm) {
        bookingForm.addEventListener('submit', handleCreateBooking);
    }
    
    // Category form handlers
    const createCategoryForm = document.getElementById('create-category-form');
    if (createCategoryForm) {
        createCategoryForm.addEventListener('submit', handleCreateCategory);
    }
    
    const updateCategoryForm = document.getElementById('update-category-form');
    if (updateCategoryForm) {
        updateCategoryForm.addEventListener('submit', handleUpdateCategory);
    }
    
    // Specialist form handlers
    const createSpecialistForm = document.getElementById('create-specialist-form');
    if (createSpecialistForm) {
        createSpecialistForm.addEventListener('submit', handleCreateSpecialist);
    }
    
    const updateSpecialistForm = document.getElementById('update-specialist-form');
    if (updateSpecialistForm) {
        updateSpecialistForm.addEventListener('submit', handleUpdateSpecialist);
    }
    
    // Slot form handlers
    const createSlotForm = document.getElementById('create-slot-form');
    if (createSlotForm) {
        createSlotForm.addEventListener('submit', handleCreateSlot);
    }
    
    const updateSlotForm = document.getElementById('update-slot-form');
    if (updateSlotForm) {
        updateSlotForm.addEventListener('submit', handleUpdateSlot);
    }
    
    // Demo login buttons
    const demoCustomerBtn = document.getElementById('demoCustomerBtn');
    if (demoCustomerBtn) {
        demoCustomerBtn.addEventListener('click', function(e) {
            e.preventDefault();
            demoLogin('CUSTOMER');
        });
    }
    
    const demoSpecialistBtn = document.getElementById('demoSpecialistBtn');
    if (demoSpecialistBtn) {
        demoSpecialistBtn.addEventListener('click', function(e) {
            e.preventDefault();
            demoLogin('SPECIALIST');
        });
    }
    
    const demoManagerBtn = document.getElementById('demoManagerBtn');
    if (demoManagerBtn) {
        demoManagerBtn.addEventListener('click', function(e) {
            e.preventDefault();
            demoLogin('MANAGER');
        });
    }
    
    // Initialize section visibility
    showSection();
}

// Initialize on DOM ready
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initializeApp);
} else {
    initializeApp();
}

// Helper: Show appropriate section based on login state
function showSection() {
    const demoSection = document.getElementById('demo-section');
    const userPanel = document.getElementById('user-panel');
    const customerSection = document.getElementById('customer-section');
    const specialistSection = document.getElementById('specialist-section');
    const managerSection = document.getElementById('manager-section');
    
    if (currentUser) {
        if (demoSection) demoSection.classList.add('hidden');
        if (userPanel) userPanel.classList.remove('hidden');
        updateUserInfo();
        
        if (customerSection) customerSection.classList.add('hidden');
        if (specialistSection) specialistSection.classList.add('hidden');
        if (managerSection) managerSection.classList.add('hidden');
        
        if (currentUser.role === 'CUSTOMER') {
            if (customerSection) customerSection.classList.remove('hidden');
            updateCustomerWarning();
            initializeCustomerDashboard();
        } else if (currentUser.role === 'SPECIALIST') {
            if (specialistSection) specialistSection.classList.remove('hidden');
            checkSpecialistProfile();
            loadMySchedule();
        } else if (currentUser.role === 'MANAGER') {
            if (managerSection) managerSection.classList.remove('hidden');
            showManagerTab('bookings');
            loadAllBookings();
        }
    } else {
        if (demoSection) {
            demoSection.classList.remove('hidden');
            resetDemoAccessSection();
        }
        if (userPanel) userPanel.classList.add('hidden');
        if (customerSection) customerSection.classList.add('hidden');
        if (specialistSection) specialistSection.classList.add('hidden');
        if (managerSection) managerSection.classList.add('hidden');
    }
}

// Initialize customer dashboard data
function initializeCustomerDashboard() {
    if (currentUser.customerId) {
        loadMyBookings();
    }
    loadSpecialists();
}

// Load specialists for customer booking
async function loadSpecialists() {
    clearDebug();
    setDebugMethod('GET');
    setDebugUrl('/api/specialists');
    
    try {
        var response = await fetch('/api/specialists');
        var data = await response.json();
        setDebugResponse(data);
        
        if (response.ok && data.success) {
            displaySpecialistsForBooking(data.data);
        } else {
            showError(extractErrorMessage(data, response));
        }
    } catch (error) {
        setDebugError(error.message);
        showError('Error loading specialists: ' + error.message);
    }
}

// Display specialists for booking selection
function displaySpecialistsForBooking(specialists) {
    var list = document.getElementById('specialists-list');
    if (!list) return;
    
    if (!specialists || specialists.length === 0) {
        list.innerHTML = '<div class="empty-state"><p>No specialists found.</p></div>';
        return;
    }
    
    var html = '';
    specialists.forEach(function(s) {
        if (!s || !s.specialistId) return;
        
        var displayName = getSpecialistDisplayName(s);
        var categoryName = getCategoryName(s);
        var levelName = getLevelName(s);
        var fee = s.fee || s.price || s.consultationFee || 0;
        
        var onclickName = escapeHtml(displayName).replace(/'/g, "\\'");
        html += '<div class="specialist-card" onclick="selectSpecialistForBooking(' + s.specialistId + ', \'' + onclickName + '\', ' + fee + ')">';
        html += '<h4>' + escapeHtml(displayName) + '</h4>';
        html += '<p>Category: ' + escapeHtml(categoryName) + '</p>';
        html += '<p>Level: ' + escapeHtml(levelName) + '</p>';
        html += '<p class="price">$' + fee + '</p>';
        html += '</div>';
    });
    list.innerHTML = html;
}

// Get specialist display name with safe fallback
function getSpecialistDisplayName(specialist) {
    return specialist?.username
        || specialist?.specialistName
        || specialist?.name
        || specialist?.user?.username
        || 'Unknown Specialist';
}

// Get category name with safe fallback
function getCategoryName(specialist) {
    return specialist?.categoryName
        || (specialist?.category && (specialist.category.name || specialist.category))
        || specialist?.category
        || '-';
}

// Get level name with safe fallback
function getLevelName(specialist) {
    return specialist?.levelName
        || (specialist?.level && (specialist.level.name || specialist.level))
        || specialist?.level
        || '-';
}

// Show manager tab and load data
function showManagerTab(tabName) {
    // Hide all tabs
    document.querySelectorAll('.tab-content').forEach(function(tab) {
        tab.classList.remove('active');
    });
    document.querySelectorAll('.tab-btn').forEach(function(btn) {
        btn.classList.remove('active');
    });
    
    // Show selected tab
    var tab = document.getElementById('tab-' + tabName);
    if (tab) {
        tab.classList.add('active');
    }
    
    // Activate button
    var buttons = document.querySelectorAll('.tab-btn');
    buttons.forEach(function(btn) {
        if (btn.textContent.toLowerCase().includes(tabName)) {
            btn.classList.add('active');
        }
    });
    
    // Load tab data
    switch (tabName) {
        case 'bookings':
            loadAllBookings();
            break;
        case 'categories':
            loadCategories();
            break;
        case 'specialists':
            loadAllSpecialistsAdmin();
            break;
        case 'slots':
            loadAllSpecialistsAdmin();
            break;
        case 'levels':
            loadLevels();
            break;
    }
}

// Update user info in header
function updateUserInfo() {
    var userInfo = document.getElementById('header-user-info');
    var roleLabel = currentUser.role.charAt(0) + currentUser.role.slice(1).toLowerCase();
    userInfo.innerHTML = '<strong>' + (currentUser.username || '-') + '</strong> (' + roleLabel + ')';
}

// Update customer warning
function updateCustomerWarning() {
    var warning = document.getElementById('customer-profile-warning');
    var warningText = document.getElementById('customer-warning-text');
    
    if (!currentUser.customerId) {
        warning.classList.remove('hidden');
        warningText.textContent = 'This customer account does not have a linked Customer profile. Booking cannot be created. Please register a new customer account or contact support.';
    } else {
        warning.classList.add('hidden');
    }
}

// Clear all selections and tables
function clearAllSelections() {
    selectedSpecialist = null;
    selectedSlot = null;
    selectedBooking = null;
    setDebugSpecialist(null);
    setDebugSlot(null);
    setDebugBooking(null);
    
    // Clear specialist selection UI (null-safe)
    const el = document.getElementById('selected-specialist-section');
    if (el) el.classList.add('hidden');
    const el2 = document.getElementById('selected-slot-section');
    if (el2) el2.classList.add('hidden');
    const el3 = document.getElementById('specialists-list');
    if (el3) el3.innerHTML = '';
    const el4 = document.getElementById('available-slots-list');
    if (el4) el4.innerHTML = '';
    const el5 = document.getElementById('booking-result');
    if (el5) el5.innerHTML = '';
    
    // Clear customer bookings
    const el6 = document.getElementById('my-bookings-list');
    if (el6) el6.innerHTML = '';
    
    // Clear reschedule section
    const el7 = document.getElementById('reschedule-section');
    if (el7) el7.classList.add('hidden');
    const el8 = document.getElementById('reschedule-result');
    if (el8) el8.innerHTML = '';
    
    // Clear specialist bookings
    const el9 = document.getElementById('specialist-bookings');
    if (el9) el9.innerHTML = '';
    
    // Clear manager tables
    const el10 = document.getElementById('all-bookings-list');
    if (el10) el10.innerHTML = '';
    const el11 = document.getElementById('categories-list');
    if (el11) el11.innerHTML = '';
    const el12 = document.getElementById('admin-specialists-list');
    if (el12) el12.innerHTML = '';
    const el13 = document.getElementById('slots-list');
    if (el13) el13.innerHTML = '';
    const el14 = document.getElementById('levels-list');
    if (el14) el14.innerHTML = '';
    
    // Clear messages
    const el15 = document.getElementById('messages');
    if (el15) el15.innerHTML = '';
}

// Demo login - calls API directly
async function demoLogin(role) {
    const creds = demoCredentials[role];
    if (!creds) {
        showError('Demo account not found for role: ' + role);
        return;
    }
    
    await loginWithCredentials(creds.username, creds.password);
}

// Login with credentials - shared function for both demo and manual login
async function loginWithCredentials(username, password) {
    clearDebug();
    clearAllSelections();
    setDebugMethod('POST');
    setDebugUrl('/api/auth/login');
    setDebugRequest({ username: username, password: password });
    
    try {
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username: username, password: password })
        });
        
        const data = await response.json();
        setDebugResponse(data);
        
        if (response.ok && data.success) {
            const user = data.data;
            currentUser = {
                userId: user.userId,
                username: user.username,
                role: user.role,
                customerId: user.customerId,
                specialistId: user.specialistId,
                managerId: user.managerId
            };
            setDebugUser(currentUser);
            showSection();
            showMessage('Login successful!');
            
            if (currentUser.role === 'CUSTOMER' && !currentUser.customerId) {
                showError('This customer account does not have a linked Customer profile.');
            }
        } else {
            showError(extractErrorMessage(data, response));
        }
    } catch (error) {
        setDebugError(error.message);
        showError('Login error: ' + error.message);
    }
}

// Login handler - for form submission
async function handleLogin(e) {
    if (e) e.preventDefault();
    
    const username = document.getElementById('login-username')?.value || '';
    const password = document.getElementById('login-password')?.value || '';
    
    await loginWithCredentials(username, password);
}

// Event delegation for demo login buttons
document.addEventListener('click', function(event) {
    const demoButton = event.target.closest('[data-demo-login]');
    if (!demoButton) return;
    
    event.preventDefault();
    
    const role = demoButton.dataset.demoLogin;
    const creds = demoCredentials[role];
    if (!creds) {
        console.error('Unknown demo login role:', role);
        return;
    }
    
    loginWithCredentials(creds.username, creds.password);
});

// Register handler
async function handleRegister(e) {
    e.preventDefault();
    
    const username = document.getElementById('reg-username').value;
    const email = document.getElementById('reg-email').value;
    const password = document.getElementById('reg-password').value;
    const address = document.getElementById('reg-address').value;
    const role = document.getElementById('reg-role').value;
    
    clearDebug();
    setDebugMethod('POST');
    setDebugUrl('/api/auth/register');
    setDebugRequest({ username, email, password, address, role });
    
    try {
        const response = await fetch('/api/auth/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, email, password, address, role })
        });
        
        const data = await response.json();
        setDebugResponse(data);
        
        if (response.ok && data.success) {
            showMessage('Registration successful! Please login.');
            document.getElementById('register-form').reset();
        } else {
            showError(extractErrorMessage(data, response));
        }
    } catch (error) {
        setDebugError(error.message);
        showError('Registration error: ' + error.message);
    }
}

// Logout
function logout() {
    currentUser = null;
    clearAllSelections();
    showSection();
    showMessage('Logged out');
}

// Toggle demo access section
function toggleDemoAccess() {
    var section = document.querySelector('.demo-access-section');
    section.classList.toggle('expanded');
}

// Reset demo access section when showing login
function resetDemoAccessSection() {
    var section = document.querySelector('.demo-access-section');
    if (section) {
        section.classList.remove('expanded');
    }
}

// Check specialist profile
function checkSpecialistProfile() {
    const warning = document.getElementById('specialist-warning');
    const content = document.getElementById('specialist-content');
    
    if (!currentUser.specialistId) {
        warning.textContent = 'This account does not have a linked specialist profile.';
        warning.classList.remove('hidden');
        content.classList.add('hidden');
    } else {
        warning.classList.add('hidden');
        content.classList.remove('hidden');
    }
}

// Update customer warning
function extractErrorMessage(data, response) {
    // Handle null/undefined data
    if (!data) {
        if (!response || !response.ok) {
            return 'Request failed: ' + (response ? 'HTTP ' + response.status : 'Unknown error');
        }
        return 'Unknown error occurred';
    }

    // Check for API response with message field
    if (typeof data === 'object') {
        // ApiResponse with message
        if (data.message && typeof data.message === 'string' && data.message.length > 0) {
            // If it's a validation error object, format it nicely
            if (data.errors && typeof data.errors === 'object') {
                var validationErrors = [];
                for (var key in data.errors) {
                    if (data.errors.hasOwnProperty(key)) {
                        validationErrors.push(key + ': ' + data.errors[key]);
                    }
                }
                if (validationErrors.length > 0) {
                    return 'Validation failed: ' + validationErrors.join('; ');
                }
            }
            return data.message;
        }

        // Direct error field
        if (data.error && typeof data.error === 'string') {
            return data.error;
        }

        // Error object from validation
        if (data.errors) {
            if (typeof data.errors === 'string') {
                return data.errors;
            }
            if (typeof data.errors === 'object') {
                var errors = [];
                for (var key in data.errors) {
                    if (data.errors.hasOwnProperty(key)) {
                        var val = data.errors[key];
                        errors.push(typeof val === 'string' ? val : key + ': ' + JSON.stringify(val));
                    }
                }
                return errors.length > 0 ? errors.join('; ') : 'Validation failed';
            }
        }
    }

    // String response
    if (typeof data === 'string') {
        if (data.length > 0 && data.length < 500) {
            return data;
        }
    }

    // HTTP status-based fallback
    if (response && !response.ok) {
        var statusMessages = {
            400: 'Bad request - invalid input or validation failed',
            401: 'Unauthorized - please login again',
            403: 'Access denied - insufficient permissions',
            404: 'Resource not found',
            409: 'Conflict - resource already exists or state conflict',
            422: 'Unprocessable entity - invalid request data',
            500: 'Internal server error'
        };
        var statusMsg = statusMessages[response.status] || 'HTTP error ' + response.status;
        if (response.statusText) {
            statusMsg += ': ' + response.statusText;
        }
        return statusMsg;
    }

    return 'Operation failed';
}

// ============= CUSTOMER FUNCTIONS =============

// Search specialists
async function searchSpecialists() {
    const keyword = document.getElementById('search-keyword').value;
    const onlyAvailable = document.getElementById('search-only-available').checked;
    
    let url = '/api/specialists/search?';
    if (keyword) url += 'keyword=' + encodeURIComponent(keyword) + '&';
    if (onlyAvailable) url += 'onlyAvailable=true';
    
    url = url.replace(/[&?]$/, '');
    
    clearDebug();
    setDebugMethod('GET');
    setDebugUrl(url);
    
    try {
        const response = await fetch(url);
        const data = await response.json();
        setDebugResponse(data);
        
        if (response.ok && data.success) {
            displaySpecialists(data.data);
            showMessage('Found ' + data.data.length + ' specialist(s)');
        } else {
            showError(extractErrorMessage(data, response));
        }
    } catch (error) {
        setDebugError(error.message);
        showError('Error loading specialists: ' + error.message);
    }
}

// Display specialists
function displaySpecialists(specialists) {
    const list = document.getElementById('specialists-list');
    
    if (!specialists || specialists.length === 0) {
        list.innerHTML = '<div class="empty-state"><p>No specialists found.</p></div>';
        return;
    }
    
    let html = '';
    
    specialists.forEach(function(sp) {
        var statusClass = sp.status === 'ACTIVE' ? 'status-active' : 'status-inactive';
        html += '<div class="specialist-card">';
        html += '<div class="specialist-info">';
        html += '<h4>' + (sp.username || '-') + '</h4>';
        html += '<div class="specialist-meta">';
        html += '<span>' + (sp.categoryName || '-') + '</span>';
        html += '<span>' + (sp.levelName || '-') + '</span>';
        html += '<span class="status-badge ' + statusClass + '">' + sp.status + '</span>';
        html += '</div>';
        html += '<div class="specialist-fee">$' + (sp.fee || '0.00') + '</div>';
        if (sp.information) {
            html += '<p>' + sp.information + '</p>';
        }
        html += '</div>';
        html += '<button onclick="selectSpecialist(' + sp.specialistId + ')" class="btn-primary">Select</button>';
        html += '</div>';
    });
    
    list.innerHTML = html;
}

// Select specialist
async function selectSpecialist(specialistId) {
    // Clear previous slot selection when selecting new specialist
    selectedSlot = null;
    setDebugSlot(null);
    document.getElementById('selected-slot-section').classList.add('hidden');
    document.getElementById('booking-result').innerHTML = '';
    
    clearDebug();
    setDebugMethod('GET');
    setDebugUrl('/api/specialists/' + specialistId);
    
    try {
        const response = await fetch('/api/specialists/' + specialistId);
        const data = await response.json();
        setDebugResponse(data);
        
        if (response.ok && data.success) {
            selectedSpecialist = data.data;
            setDebugSpecialist(selectedSpecialist);
            displaySelectedSpecialist();
            loadAvailableSlots(specialistId);
            showMessage('Specialist selected: ' + selectedSpecialist.username);
        } else {
            showError(extractErrorMessage(data, response));
        }
    } catch (error) {
        setDebugError(error.message);
        showError('Error loading specialist: ' + error.message);
    }
}

// Display selected specialist
function displaySelectedSpecialist() {
    const section = document.getElementById('selected-specialist-section');
    const div = document.getElementById('selected-specialist');
    
    if (!selectedSpecialist) {
        section.classList.add('hidden');
        return;
    }
    
    section.classList.remove('hidden');
    div.innerHTML = '<div class="specialist-info">' +
        '<h4>' + (selectedSpecialist.username || '-') + '</h4>' +
        '<div class="specialist-meta">' +
        '<span>' + (selectedSpecialist.categoryName || '-') + '</span>' +
        '<span>' + (selectedSpecialist.levelName || '-') + '</span>' +
        '</div>' +
        '<div class="specialist-fee">$' + (selectedSpecialist.fee || '0.00') + ' per consultation</div>' +
        '</div>';
}

// Clear specialist selection
function clearSpecialist() {
    selectedSpecialist = null;
    selectedSlot = null;
    setDebugSpecialist(null);
    setDebugSlot(null);
    document.getElementById('selected-specialist-section').classList.add('hidden');
    document.getElementById('selected-slot-section').classList.add('hidden');
    document.getElementById('booking-result').innerHTML = '';
    document.getElementById('available-slots-list').innerHTML = '';
    showMessage('Specialist selection cleared');
}

// Load available slots
async function loadAvailableSlots(specialistId) {
    clearDebug();
    setDebugMethod('GET');
    setDebugUrl('/api/slots/specialist/' + specialistId + '/available');
    
    try {
        const response = await fetch('/api/slots/specialist/' + specialistId + '/available');
        const data = await response.json();
        setDebugResponse(data);
        
        if (response.ok && data.success) {
            displayAvailableSlots(data.data);
        } else {
            showError(extractErrorMessage(data, response));
        }
    } catch (error) {
        setDebugError(error.message);
        showError('Error loading slots: ' + error.message);
    }
}

// Display available slots in timetable format
function displayAvailableSlots(slots) {
    const timetable = document.getElementById('available-slots-timetable');
    const today = new Date().toISOString().split('T')[0];
    
    if (!slots || slots.length === 0) {
        timetable.innerHTML = '<div class="empty-state"><p>No available slots found.</p></div>';
        return;
    }
    
    // Group slots by date
    const slotsByDate = {};
    slots.forEach(function(slot) {
        const date = slot.appointmentDate;
        if (!slotsByDate[date]) {
            slotsByDate[date] = [];
        }
        slotsByDate[date].push(slot);
    });
    
    // Sort dates
    const sortedDates = Object.keys(slotsByDate).sort();
    
    // Limit to next 14 days
    const upcomingDates = sortedDates.slice(0, 14);
    
    if (upcomingDates.length === 0) {
        timetable.innerHTML = '<div class="empty-state"><p>No upcoming slots available.</p></div>';
        return;
    }
    
    // Build timetable HTML
    let html = '';
    upcomingDates.forEach(function(date) {
        const daySlots = slotsByDate[date];
        const dateObj = new Date(date);
        const dayNames = ['SUN', 'MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT'];
        const dayName = dayNames[dateObj.getDay()];
        const month = String(dateObj.getMonth() + 1).padStart(2, '0');
        const dayOfMonth = String(dateObj.getDate()).padStart(2, '0');
        const dateStr = month + '-' + dayOfMonth;
        const isToday = date === today;
        
        html += '<div class="timetable-day' + (isToday ? ' today' : '') + '">';
        html += '<div class="timetable-day-header">';
        html += '<div class="timetable-day-name">' + dayName + '</div>';
        html += '<div class="timetable-day-date">' + dateStr + '</div>';
        html += '</div>';
        html += '<div class="timetable-day-content">';
        
        if (daySlots.length === 0) {
            html += '<div class="timetable-empty">No slots</div>';
        } else {
            daySlots.forEach(function(slot) {
                const timeStr = formatTime(slot.startTime) + ' - ' + formatTime(slot.endTime);
                const isSelected = selectedSlot && selectedSlot.slotId === slot.slotId && selectedSlot.appointmentDate === slot.appointmentDate;
                const occurrenceStatus = slot.occurrenceStatus || 'AVAILABLE';
                const isBookable = slot.bookable !== false && occurrenceStatus === 'AVAILABLE';
                
                if (isBookable) {
                    // Available slot - clickable
                    html += '<button class="time-chip' + (isSelected ? ' selected' : '') + '" ';
                    html += 'onclick="selectSlot(' + slot.slotId + ', \'' + slot.appointmentDate + '\', \'' + slot.startTime + '\', \'' + slot.endTime + '\')">';
                    html += timeStr + '</button>';
                } else {
                    // Occupied slot - disabled with status
                    const statusLabel = occurrenceStatus.charAt(0) + occurrenceStatus.slice(1).toLowerCase();
                    html += '<button class="time-chip disabled" disabled ';
                    html += 'title="' + statusLabel + ' - Not available">';
                    html += timeStr + ' <span class="slot-status-badge">' + statusLabel + '</span></button>';
                }
            });
        }
        
        html += '</div></div>';
    });
    
    timetable.innerHTML = html;
}

// Legacy function - kept for compatibility
function displayAvailableSlotsOld(slots) {
    displayAvailableSlots(slots);
}

// Format time to HH:MM
function formatTime(time) {
    if (!time) return '-';
    if (time.length > 5) return time.substring(0, 5);
    return time;
}

// Select slot
function selectSlot(slotId, appointmentDate, startTime, endTime) {
    selectedSlot = {
        slotId: slotId,
        appointmentDate: appointmentDate,
        startTime: startTime,
        endTime: endTime,
        specialistId: selectedSpecialist.specialistId
    };
    setDebugSlot(selectedSlot);
    displaySelectedSlot();
    document.getElementById('booking-result').innerHTML = '';
}

// Display selected slot
function displaySelectedSlot() {
    const section = document.getElementById('selected-slot-section');
    const summaryDiv = document.getElementById('booking-summary');
    
    if (!selectedSlot || !selectedSpecialist) {
        section.classList.add('hidden');
        return;
    }
    
    section.classList.remove('hidden');
    
    // Show booking summary
    var timeStr = formatTime(selectedSlot.startTime) + ' - ' + formatTime(selectedSlot.endTime);
    var price = selectedSpecialist.fee ? '$' + parseFloat(selectedSpecialist.fee).toFixed(2) : '$0.00';
    
    summaryDiv.innerHTML = '<div class="booking-summary">' +
        '<div class="booking-summary-item"><strong>Specialist</strong><span>' + (selectedSpecialist.username || '-') + '</span></div>' +
        '<div class="booking-summary-item"><strong>Category</strong><span>' + (selectedSpecialist.categoryName || '-') + '</span></div>' +
        '<div class="booking-summary-item"><strong>Appointment Date</strong><span>' + (selectedSlot.appointmentDate || '-') + '</span></div>' +
        '<div class="booking-summary-item"><strong>Time</strong><span>' + timeStr + '</span></div>' +
        '<div class="booking-summary-price">Consultation Fee: ' + price + '</div>' +
        '</div>';
}

// Clear slot selection
function clearSlot() {
    selectedSlot = null;
    setDebugSlot(null);
    document.getElementById('selected-slot-section').classList.add('hidden');
    document.getElementById('booking-result').innerHTML = '';
    showMessage('Slot selection cleared');
}

// Create booking
async function handleCreateBooking(e) {
    e.preventDefault();
    
    // Validation
    var validationErrors = [];
    
    if (!currentUser) {
        validationErrors.push('No user logged in');
    }
    
    if (currentUser && currentUser.role !== 'CUSTOMER') {
        validationErrors.push('Only CUSTOMER role can create bookings');
    }
    
    if (currentUser && !currentUser.customerId) {
        validationErrors.push('This customer account does not have a linked Customer profile. Booking cannot be created.');
    }
    
    if (!selectedSpecialist) {
        validationErrors.push('Please select a specialist first');
    }
    
    if (!selectedSlot) {
        validationErrors.push('Please select a slot first');
    }
    
    var topic = document.getElementById('booking-topic').value;
    if (!topic || topic.trim() === '') {
        validationErrors.push('Topic is required');
    }
    
    if (validationErrors.length > 0) {
        showError(validationErrors.join('; '));
        return;
    }
    
    var notes = document.getElementById('booking-notes').value;
    
    var requestBody = {
        customerId: currentUser.customerId,
        specialistId: selectedSpecialist.specialistId,
        slotId: selectedSlot.slotId,
        appointmentDate: selectedSlot.appointmentDate,
        topic: topic,
        notes: notes
    };
    
    clearDebug();
    setDebugMethod('POST');
    setDebugUrl('/api/bookings');
    setDebugRequest(requestBody);
    
    var bookingResult = document.getElementById('booking-result');
    bookingResult.innerHTML = '<p><em>Creating booking...</em></p>';
    
    try {
        var response = await fetch('/api/bookings', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(requestBody)
        });
        
        var data = await response.json();
        setDebugResponse(data);
        
        if (response.ok && data.success) {
            displayBookingResult(data.data);
            document.getElementById('booking-form').reset();
            showMessage('Booking created successfully! Status: ' + data.data.status);
            
            // Clear selected slot since it's now booked
            selectedSlot = null;
            setDebugSlot(null);
            document.getElementById('selected-slot-section').classList.add('hidden');
            
            // Refresh available slots for the selected specialist
            if (selectedSpecialist && selectedSpecialist.specialistId) {
                loadAvailableSlots(selectedSpecialist.specialistId);
            }
            
            // Refresh my bookings
            loadMyBookings();
        } else {
            var errorMsg = extractErrorMessage(data, response);
            bookingResult.innerHTML = '<p style="color:red"><strong>Error:</strong> ' + errorMsg + '</p>';
            setDebugError(errorMsg);
            showError(errorMsg);
            
            // If slot is not available, clear selection and refresh
            if (errorMsg.indexOf('not available') !== -1 || errorMsg.indexOf('occupied') !== -1 || errorMsg.indexOf('already been booked') !== -1) {
                selectedSlot = null;
                setDebugSlot(null);
                document.getElementById('selected-slot-section').classList.add('hidden');
                if (selectedSpecialist && selectedSpecialist.specialistId) {
                    loadAvailableSlots(selectedSpecialist.specialistId);
                }
            }
        }
    } catch (error) {
        bookingResult.innerHTML = '<p style="color:red"><strong>Error:</strong> ' + error.message + '</p>';
        setDebugError(error.message);
        showError('Error creating booking: ' + error.message);
    }
}

// Display booking result
function displayBookingResult(booking) {
    var result = document.getElementById('booking-result');
    var slotTime = formatSlotDateTime(booking);
    result.innerHTML = '<h4>Booking Created Successfully</h4>' +
        '<p><strong>Booking ID:</strong> ' + (booking.bookingId || '-') + '</p>' +
        '<p><strong>Status:</strong> ' + (booking.status || '-') + '</p>' +
        '<p><strong>Price:</strong> ' + (booking.price || '-') + '</p>' +
        '<p><strong>Specialist:</strong> ' + (booking.specialistName || '-') + '</p>' +
        '<p><strong>Slot:</strong> ' + slotTime + '</p>' +
        '<p><strong>Topic:</strong> ' + (booking.topic || '-') + '</p>' +
        '<p><strong>Notes:</strong> ' + (booking.notes || '-') + '</p>';
}

// Format slot date/time from booking response
function formatSlotDateTime(booking) {
    if (booking.slotDateTime) {
        return booking.slotDateTime;
    }
    if (booking.slotDate) {
        var time = '';
        if (booking.slotStartTime) {
            time = booking.slotStartTime;
            if (booking.slotEndTime) {
                time += ' - ' + booking.slotEndTime;
            }
        }
        return booking.slotDate + (time ? ' ' + time : '');
    }
    return '-';
}

// Load my bookings
async function loadMyBookings() {
    if (!currentUser || !currentUser.customerId) {
        showError('No customer profile linked to this account');
        return;
    }
    
    clearDebug();
    setDebugMethod('GET');
    setDebugUrl('/api/bookings/customer/' + currentUser.customerId);
    
    try {
        var response = await fetch('/api/bookings/customer/' + currentUser.customerId);
        var data = await response.json();
        setDebugResponse(data);
        
        if (response.ok && data.success) {
            displayMyBookings(data.data);
        } else {
            showError(extractErrorMessage(data, response));
        }
    } catch (error) {
        setDebugError(error.message);
        showError('Error loading bookings: ' + error.message);
    }
}

// Display my bookings
function displayMyBookings(bookings) {
    var list = document.getElementById('my-bookings-list');
    
    if (!bookings || bookings.length === 0) {
        list.innerHTML = '<div class="empty-state"><p>No bookings found.</p></div>';
        return;
    }
    
    var html = '';
    
    // Sort bookings by date/time descending (most recent first)
    bookings.sort(function(a, b) {
        var dateA = a.appointmentDate || '1970-01-01';
        var dateB = b.appointmentDate || '1970-01-01';
        var timeA = a.slotStartTime || '00:00';
        var timeB = b.slotStartTime || '00:00';
        if (dateA !== dateB) return dateB.localeCompare(dateA);
        return timeB.localeCompare(timeA);
    });
    
    bookings.forEach(function(b) {
        var formatted = formatBookingDateTime(b);
        var statusLabel = getStatusLabel(b.status);
        var statusClass = getStatusClass(b.status);
        var dateTimeStr = formatted.date + ' ' + formatted.time;
        
        html += '<div class="booking-item">';
        html += '<div class="booking-item-header">';
        html += '<div class="booking-item-info">';
        html += '<h4>' + (b.specialistName || '-') + '</h4>';
        html += '<div class="booking-item-meta">';
        html += '<span>' + dateTimeStr + '</span>';
        html += '<span>$' + (b.price || '0.00') + '</span>';
        html += '<span class="status-badge ' + statusClass + '">' + statusLabel + '</span>';
        html += '</div>';
        html += '</div>';
        html += '<div class="booking-item-actions">';
        if (b.status === 'PENDING' || b.status === 'CONFIRMED') {
            html += '<button onclick="prepareReschedule(' + b.bookingId + ', ' + b.specialistId + ', ' + b.slotId + ', \'' + b.appointmentDate + '\', \'' + b.slotStartTime + '\', \'' + b.slotEndTime + '\')" class="btn-small">Reschedule</button>';
            html += '<button onclick="cancelBookingCustomer(' + b.bookingId + ')" class="btn-small">Cancel</button>';
        }
        html += '</div>';
        html += '</div>';
        if (b.topic) {
            html += '<p style="font-size:13px;color:#6e6e73;margin-top:8px;"><strong>Topic:</strong> ' + b.topic + '</p>';
        }
        if (b.notes) {
            html += '<p style="font-size:13px;color:#6e6e73;"><strong>Notes:</strong> ' + b.notes + '</p>';
        }
        html += '</div>';
    });
    
    list.innerHTML = html;
}

// Get user-friendly status label
function getStatusLabel(status) {
    const labels = {
        'PENDING': 'Waiting for confirmation',
        'CONFIRMED': 'Confirmed',
        'COMPLETED': 'Completed',
        'CANCELLED': 'Cancelled'
    };
    return labels[status] || status;
}

// Get status CSS class
function getStatusClass(status) {
    const classes = {
        'PENDING': 'status-badge-pending',
        'CONFIRMED': 'status-badge-confirmed',
        'COMPLETED': 'status-badge-completed',
        'CANCELLED': 'status-badge-cancelled'
    };
    return classes[status] || '';
}

// Select booking for details
function selectBookingForDetails(bookingId, status) {
    selectedBooking = { bookingId: bookingId, status: status };
    setDebugBooking(selectedBooking);
    showMessage('Booking ' + bookingId + ' selected for details');
}

// Prepare reschedule
async function prepareReschedule(bookingId, specialistId, slotId, appointmentDate, startTime, endTime) {
    // Store full booking info for filtering and reschedule
    selectedBooking = { 
        bookingId: bookingId, 
        specialistId: specialistId,
        slotId: slotId,
        appointmentDate: appointmentDate,
        startTime: startTime,
        endTime: endTime
    };
    setDebugBooking(selectedBooking);
    
    document.getElementById('reschedule-section').classList.remove('hidden');
    
    clearDebug();
    setDebugMethod('GET');
    setDebugUrl('/api/slots/specialist/' + specialistId + '/available');
    
    try {
        var response = await fetch('/api/slots/specialist/' + specialistId + '/available');
        var data = await response.json();
        setDebugResponse(data);
        
        if (response.ok && data.success) {
            displayRescheduleSlots(data.data);
        } else {
            showError(extractErrorMessage(data, response));
        }
    } catch (error) {
        setDebugError(error.message);
        showError('Error loading slots: ' + error.message);
    }
}

// Helper: Normalize status from any possible field
function normalizeStatus(slot) {
    return String(
        slot.occurrenceStatus ||
        slot.status ||
        slot.bookingStatus ||
        slot.availabilityStatus ||
        ''
    ).toUpperCase();
}

// Helper: Check if a slot is bookable for rescheduling (STRICT)
function isSlotBookable(slot) {
    // Explicitly not bookable
    if (slot.bookable === false || slot.bookable === 'false') {
        return false;
    }
    
    var status = normalizeStatus(slot);
    
    // Block these statuses - these are NEVER selectable for reschedule
    var blockedStatuses = ['PENDING', 'CONFIRMED', 'COMPLETED', 'BOOKED', 'UNAVAILABLE'];
    if (blockedStatuses.indexOf(status) !== -1) {
        return false;
    }
    
    // Must be explicitly AVAILABLE or CANCELLED with bookable=true
    if (slot.bookable === true) {
        return true;
    }
    
    // If no explicit bookable flag, only allow AVAILABLE or CANCELLED status
    if (status === 'AVAILABLE' || status === 'CANCELLED') {
        return true;
    }
    
    // Reject if no status info at all
    return false;
}

// Helper: Check if slot is the same occurrence as the booking
function isSameOccurrence(slot, booking) {
    if (!booking) return false;
    
    var slotDate = slot.appointmentDate || slot.slotDate;
    var bookingDate = booking.appointmentDate || booking.slotDate;
    var slotStart = slot.startTime || slot.slotStartTime;
    var slotEnd = slot.endTime || slot.slotEndTime;
    var bookingStart = booking.startTime || booking.slotStartTime;
    var bookingEnd = booking.endTime || booking.slotEndTime;
    
    // Compare as strings for safety
    return String(slot.slotId) === String(booking.slotId)
        && String(slotDate) === String(bookingDate)
        && String(slotStart) === String(bookingStart)
        && String(slotEnd) === String(bookingEnd);
}

// Display reschedule slots - only show bookable slots
function displayRescheduleSlots(slots) {
    var list = document.getElementById('reschedule-slots');
    
    if (!slots || slots.length === 0) {
        list.innerHTML = '<p class="empty-message">No available slots for rescheduling.</p>';
        return;
    }
    
    // Filter to only bookable slots and exclude the current booking's occurrence
    var availableSlots = slots.filter(function(slot) {
        // Must be bookable
        if (!isSlotBookable(slot)) return false;
        
        // Exclude the current booking's occurrence
        if (isSameOccurrence(slot, selectedBooking)) return false;
        
        return true;
    });
    
    if (availableSlots.length === 0) {
        list.innerHTML = '<p class="empty-message">No available slots for rescheduling.</p>';
        return;
    }
    
    // Group by date for better UX
    var slotsByDate = {};
    availableSlots.forEach(function(slot) {
        var date = slot.appointmentDate || slot.slotDate;
        if (!slotsByDate[date]) {
            slotsByDate[date] = [];
        }
        slotsByDate[date].push(slot);
    });
    
    // Sort dates
    var sortedDates = Object.keys(slotsByDate).sort();
    
    var html = '<table><thead><tr>';
    html += '<th>Select</th><th>Date</th><th>Day</th><th>Time</th>';
    html += '</tr></thead><tbody>';
    
    var dayNames = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
    
    sortedDates.forEach(function(date) {
        var daySlots = slotsByDate[date];
        // Sort by start time
        daySlots.sort(function(a, b) {
            var aStart = a.startTime || a.slotStartTime;
            var bStart = b.startTime || b.slotStartTime;
            return aStart.localeCompare(bStart);
        });
        
        // Add date row
        var dateObj = new Date(date);
        var dayName = dayNames[dateObj.getDay()];
        
        daySlots.forEach(function(slot) {
            var startTime = slot.startTime || slot.slotStartTime;
            var endTime = slot.endTime || slot.slotEndTime;
            var timeStr = formatTime(startTime) + ' - ' + formatTime(endTime);
            html += '<tr>';
            html += '<td><input type="radio" name="reschedule-slot" value="' + slot.slotId + 
                    '" data-date="' + (slot.appointmentDate || slot.slotDate) + 
                    '" data-start="' + startTime + 
                    '" data-end="' + endTime + '"></td>';
            html += '<td>' + (slot.appointmentDate || slot.slotDate) + '</td>';
            html += '<td>' + dayName + '</td>';
            html += '<td>' + timeStr + '</td>';
            html += '</tr>';
        });
    });
    
    html += '</tbody></table>';
    list.innerHTML = html;
}

// Confirm reschedule
async function confirmReschedule() {
    if (!selectedBooking) {
        showError('Please select a booking to reschedule');
        return;
    }
    
    var selectedRadio = document.querySelector('input[name="reschedule-slot"]:checked');
    if (!selectedRadio) {
        showError('Please select a new slot');
        return;
    }
    
    var newSlotId = parseInt(selectedRadio.value);
    var newAppointmentDate = selectedRadio.getAttribute('data-date');
    
    clearDebug();
    setDebugMethod('PUT');
    setDebugUrl('/api/bookings/' + selectedBooking.bookingId + '/reschedule');
    setDebugRequest({ newSlotId: newSlotId, newAppointmentDate: newAppointmentDate });
    
    try {
        var response = await fetch('/api/bookings/' + selectedBooking.bookingId + '/reschedule', {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ newSlotId: newSlotId, newAppointmentDate: newAppointmentDate })
        });
        
        var data = await response.json();
        setDebugResponse(data);
        
        if (response.ok && data.success) {
            document.getElementById('reschedule-result').innerHTML = '<h4>Reschedule Successful</h4>' +
                '<p><strong>Booking ID:</strong> ' + data.data.bookingId + '</p>' +
                '<p><strong>New Status:</strong> ' + data.data.status + '</p>' +
                '<p><em>Note: Status returns to PENDING and needs manager confirmation again.</em></p>';
            document.getElementById('reschedule-section').classList.add('hidden');
            loadMyBookings();
            
            // Refresh available slots for the specialist
            if (selectedBooking.specialistId) {
                loadAvailableSlots(selectedBooking.specialistId);
            }
            
            showMessage('Reschedule successful!');
        } else {
            showError(extractErrorMessage(data, response));
        }
    } catch (error) {
        setDebugError(error.message);
        showError('Error rescheduling: ' + error.message);
    }
}

// Cancel booking (customer)
async function cancelBookingCustomer(bookingId) {
    if (!confirm('Are you sure you want to cancel this booking?')) {
        return;
    }
    
    clearDebug();
    setDebugMethod('PUT');
    setDebugUrl('/api/bookings/' + bookingId + '/cancel');
    
    try {
        var response = await fetch('/api/bookings/' + bookingId + '/cancel', {
            method: 'PUT'
        });
        
        var data = await response.json();
        setDebugResponse(data);
        
        if (response.ok && data.success) {
            showMessage('Booking cancelled successfully');
            loadMyBookings();
            
            // Refresh available slots if a specialist is selected
            if (selectedSpecialist && selectedSpecialist.specialistId) {
                loadAvailableSlots(selectedSpecialist.specialistId);
            }
        } else {
            showError(extractErrorMessage(data, response));
        }
    } catch (error) {
        setDebugError(error.message);
        showError('Error cancelling booking: ' + error.message);
    }
}

// ============= SPECIALIST FUNCTIONS =============

// Format booking date/time for display
function formatBookingDateTime(booking) {
    var parsed = parseSlotDateTime(booking);
    var dateStr = parsed.appointmentDate || '-';
    var startTimeStr = parsed.slotStartTime ? formatTime(parsed.slotStartTime) : null;
    var endTimeStr = parsed.slotEndTime ? formatTime(parsed.slotEndTime) : null;
    
    var timeStr = '-';
    if (startTimeStr) {
        timeStr = startTimeStr;
        if (endTimeStr) {
            timeStr += ' - ' + endTimeStr;
        }
    }
    
    return { date: dateStr, time: timeStr };
}

// Parse slot data into date and time components
function parseSlotDateTime(booking) {
    if (booking.appointmentDate) {
        return {
            appointmentDate: booking.appointmentDate,
            slotStartTime: booking.slotStartTime,
            slotEndTime: booking.slotEndTime
        };
    }
    if (booking.slotDateTime) {
        var dt = booking.slotDateTime;
        if (typeof dt === 'string') {
            var parts = dt.split('T');
            return {
                appointmentDate: parts[0],
                slotStartTime: parts[1] ? parts[1].substring(0, 5) : null,
                slotEndTime: null
            };
        }
    }
    return { appointmentDate: null, slotStartTime: null, slotEndTime: null };
}

// Load my schedule
async function loadMySchedule() {
    if (!currentUser || !currentUser.specialistId) {
        showError('No specialist profile linked to this account');
        return;
    }
    
    clearDebug();
    setDebugMethod('GET');
    setDebugUrl('/api/bookings/specialist/' + currentUser.specialistId);
    
    try {
        var response = await fetch('/api/bookings/specialist/' + currentUser.specialistId);
        var data = await response.json();
        setDebugResponse(data);
        
        if (response.ok && data.success) {
            displaySpecialistBookings(data.data);
        } else {
            showError(extractErrorMessage(data, response));
        }
    } catch (error) {
        setDebugError(error.message);
        showError('Error loading schedule: ' + error.message);
    }
}

// Display specialist bookings
function displaySpecialistBookings(bookings) {
    var list = document.getElementById('specialist-bookings');
    
    if (!bookings || bookings.length === 0) {
        list.innerHTML = '<div class="empty-state"><p>No bookings found.</p></div>';
        return;
    }
    
    var html = '<table><thead><tr>';
    html += '<th>Date</th><th>Time</th><th>Customer</th><th>Topic</th><th>Status</th><th>Price</th><th>Action</th>';
    html += '</tr></thead><tbody>';
    
    // Sort by date/time ascending (upcoming first)
    bookings.sort(function(a, b) {
        var dateA = a.appointmentDate || '1970-01-01';
        var dateB = b.appointmentDate || '1970-01-01';
        var timeA = a.slotStartTime || '00:00';
        var timeB = b.slotStartTime || '00:00';
        if (dateA !== dateB) return dateA.localeCompare(dateB);
        return timeA.localeCompare(timeB);
    });
    
    bookings.forEach(function(b) {
        var formatted = formatBookingDateTime(b);
        var statusLabel = getStatusLabel(b.status);
        var statusClass = getStatusClass(b.status);
        
        html += '<tr>';
        html += '<td>' + formatted.date + '</td>';
        html += '<td>' + formatted.time + '</td>';
        html += '<td>' + (b.customerName || '-') + '</td>';
        html += '<td>' + (b.topic || '-') + '</td>';
        html += '<td><span class="status-badge ' + statusClass + '">' + statusLabel + '</span></td>';
        html += '<td>$' + (b.price || '0.00') + '</td>';
        
        var actions = '';
        if (b.status === 'CONFIRMED') {
            actions += '<button onclick="completeBookingSpecialist(' + b.bookingId + ')" class="btn-small">Complete</button>';
        }
        html += '<td>' + actions + '</td>';
        html += '</tr>';
    });
    
    html += '</tbody></table>';
    list.innerHTML = html;
}

// Complete booking (specialist)
async function completeBookingSpecialist(bookingId) {
    if (!confirm('Mark this booking as completed? The slot will become available again.')) {
        return;
    }
    
    clearDebug();
    setDebugMethod('PUT');
    setDebugUrl('/api/bookings/' + bookingId + '/complete');
    
    try {
        var response = await fetch('/api/bookings/' + bookingId + '/complete', {
            method: 'PUT'
        });
        
        var data = await response.json();
        setDebugResponse(data);
        
        if (response.ok && data.success) {
            showMessage('Booking completed successfully. Slot is now available.');
            loadMySchedule();
        } else {
            showError(extractErrorMessage(data, response));
        }
    } catch (error) {
        setDebugError(error.message);
        showError('Error completing booking: ' + error.message);
    }
}

// ============= MANAGER FUNCTIONS =============

// Load all bookings
async function loadAllBookings() {
    clearDebug();
    setDebugMethod('GET');
    setDebugUrl('/api/bookings');
    
    try {
        var response = await fetch('/api/bookings');
        var data = await response.json();
        setDebugResponse(data);
        
        if (response.ok && data.success) {
            displayAllBookings(data.data);
        } else {
            showError(extractErrorMessage(data, response));
        }
    } catch (error) {
        setDebugError(error.message);
        showError('Error loading bookings: ' + error.message);
    }
}

// Display all bookings
function displayAllBookings(bookings) {
    var list = document.getElementById('all-bookings-list');
    
    if (!bookings || bookings.length === 0) {
        list.innerHTML = '<div class="empty-state"><p>No bookings found.</p></div>';
        return;
    }
    
    var html = '<table><thead><tr>';
    html += '<th>ID</th><th>Customer</th><th>Specialist</th><th>Date</th><th>Time</th><th>Topic</th><th>Status</th><th>Price</th><th>Action</th>';
    html += '</tr></thead><tbody>';
    
    // Sort by date/time ascending
    bookings.sort(function(a, b) {
        var dateA = a.appointmentDate || '1970-01-01';
        var dateB = b.appointmentDate || '1970-01-01';
        var timeA = a.slotStartTime || '00:00';
        var timeB = b.slotStartTime || '00:00';
        if (dateA !== dateB) return dateA.localeCompare(dateB);
        return timeA.localeCompare(timeB);
    });
    
    bookings.forEach(function(b) {
        var formatted = formatBookingDateTime(b);
        var statusLabel = getStatusLabel(b.status);
        var statusClass = getStatusClass(b.status);
        
        html += '<tr>';
        html += '<td>' + b.bookingId + '</td>';
        html += '<td>' + (b.customerName || '-') + '</td>';
        html += '<td>' + (b.specialistName || '-') + '</td>';
        html += '<td>' + formatted.date + '</td>';
        html += '<td>' + formatted.time + '</td>';
        html += '<td>' + (b.topic || '-') + '</td>';
        html += '<td><span class="status-badge ' + statusClass + '">' + statusLabel + '</span></td>';
        html += '<td>$' + (b.price || '0.00') + '</td>';
        
        var actions = '';
        if (b.status === 'PENDING') {
            actions += '<button onclick="confirmBookingManager(' + b.bookingId + ')" class="btn-small">Confirm</button> ';
            actions += '<button onclick="cancelBookingManager(' + b.bookingId + ')" class="btn-small">Cancel</button>';
        } else if (b.status === 'CONFIRMED') {
            actions += '<button onclick="cancelBookingManager(' + b.bookingId + ')" class="btn-small">Cancel</button>';
        }
        html += '<td>' + actions + '</td>';
        html += '</tr>';
    });
    
    html += '</tbody></table>';
    list.innerHTML = html;
}

// Confirm booking (manager)
async function confirmBookingManager(bookingId) {
    clearDebug();
    setDebugMethod('PUT');
    setDebugUrl('/api/bookings/' + bookingId + '/confirm');
    
    try {
        var response = await fetch('/api/bookings/' + bookingId + '/confirm', {
            method: 'PUT'
        });
        
        var data = await response.json();
        setDebugResponse(data);
        
        if (response.ok && data.success) {
            showMessage('Booking confirmed successfully');
            loadAllBookings();
        } else {
            showError(extractErrorMessage(data, response));
        }
    } catch (error) {
        setDebugError(error.message);
        showError('Error confirming booking: ' + error.message);
    }
}

// Cancel booking (manager)
async function cancelBookingManager(bookingId) {
    if (!confirm('Are you sure you want to cancel this booking?')) {
        return;
    }
    
    clearDebug();
    setDebugMethod('PUT');
    setDebugUrl('/api/bookings/' + bookingId + '/cancel');
    
    try {
        var response = await fetch('/api/bookings/' + bookingId + '/cancel', {
            method: 'PUT'
        });
        
        var data = await response.json();
        setDebugResponse(data);
        
        if (response.ok && data.success) {
            showMessage('Booking cancelled successfully');
            loadAllBookings();
        } else {
            showError(extractErrorMessage(data, response));
        }
    } catch (error) {
        setDebugError(error.message);
        showError('Error cancelling booking: ' + error.message);
    }
}

// ============= CATEGORY MANAGEMENT =============

// Load categories
async function loadCategories() {
    clearDebug();
    setDebugMethod('GET');
    setDebugUrl('/api/admin/expertise-categories');
    
    try {
        var response = await fetch('/api/admin/expertise-categories');
        var data = await response.json();
        setDebugResponse(data);
        
        if (response.ok && data.success) {
            displayCategories(data.data);
        } else {
            showError(extractErrorMessage(data, response));
        }
    } catch (error) {
        setDebugError(error.message);
        showError('Error loading categories: ' + error.message);
    }
}

// Display categories
function displayCategories(categories) {
    var list = document.getElementById('categories-list');
    
    if (!categories || categories.length === 0) {
        list.innerHTML = '<p>No categories found.</p>';
        return;
    }
    
    var html = '<table><thead><tr>';
    html += '<th>ID</th><th>Name</th><th>Status</th><th>Action</th>';
    html += '</tr></thead><tbody>';
    
    categories.forEach(function(cat) {
        html += '<tr>';
        html += '<td>' + cat.categoryId + '</td>';
        html += '<td>' + (cat.name || '-') + '</td>';
        html += '<td>' + cat.status + '</td>';
        var actionBtn = cat.status === 'ACTIVE' 
            ? '<button onclick="deactivateCategory(' + cat.categoryId + ')">Deactivate</button>'
            : '<button disabled>Inactive</button>';
        html += '<td>' + actionBtn + '</td>';
        html += '</tr>';
    });
    
    html += '</tbody></table>';
    list.innerHTML = html;
}

// Create category
async function handleCreateCategory(e) {
    e.preventDefault();
    
    var name = document.getElementById('cat-name').value;
    var status = document.getElementById('cat-status').value;
    
    if (!name || name.trim() === '') {
        showError('Category name is required');
        return;
    }
    
    var requestBody = { name: name, status: status };
    
    clearDebug();
    setDebugMethod('POST');
    setDebugUrl('/api/admin/expertise-categories');
    setDebugRequest(requestBody);
    
    try {
        var response = await fetch('/api/admin/expertise-categories', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(requestBody)
        });
        
        var data = await response.json();
        setDebugResponse(data);
        
        if (response.ok && data.success) {
            showMessage('Category created successfully');
            document.getElementById('create-category-form').reset();
            loadCategories();
        } else {
            showError(extractErrorMessage(data, response));
        }
    } catch (error) {
        setDebugError(error.message);
        showError('Error creating category: ' + error.message);
    }
}

// Update category
async function handleUpdateCategory(e) {
    e.preventDefault();
    
    var categoryId = document.getElementById('update-cat-id').value;
    var name = document.getElementById('update-cat-name').value;
    var status = document.getElementById('update-cat-status').value;
    
    var requestBody = {};
    if (name) requestBody.name = name;
    if (status) requestBody.status = status;
    
    clearDebug();
    setDebugMethod('PUT');
    setDebugUrl('/api/admin/expertise-categories/' + categoryId);
    setDebugRequest(requestBody);
    
    try {
        var response = await fetch('/api/admin/expertise-categories/' + categoryId, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(requestBody)
        });
        
        var data = await response.json();
        setDebugResponse(data);
        
        if (response.ok && data.success) {
            showMessage('Category updated successfully');
            document.getElementById('update-category-form').reset();
            loadCategories();
        } else {
            showError(extractErrorMessage(data, response));
        }
    } catch (error) {
        setDebugError(error.message);
        showError('Error updating category: ' + error.message);
    }
}

// Deactivate category - FIXED to use DELETE endpoint
async function deactivateCategory(categoryId) {
    if (!confirm('Are you sure you want to deactivate this category?')) {
        return;
    }
    
    clearDebug();
    setDebugMethod('DELETE');
    setDebugUrl('/api/admin/expertise-categories/' + categoryId);
    
    try {
        var response = await fetch('/api/admin/expertise-categories/' + categoryId, {
            method: 'DELETE'
        });
        
        var data = await response.json();
        setDebugResponse(data);
        
        if (response.ok && data.success) {
            showMessage('Category deactivated successfully');
            loadCategories();
        } else {
            showError(extractErrorMessage(data, response));
        }
    } catch (error) {
        setDebugError(error.message);
        showError('Error deactivating category: ' + error.message);
    }
}

// ============= SPECIALIST MANAGEMENT (ADMIN) =============

// Load all specialists (admin)
async function loadAllSpecialistsAdmin() {
    clearDebug();
    setDebugMethod('GET');
    setDebugUrl('/api/specialists');
    
    try {
        var response = await fetch('/api/specialists');
        var data = await response.json();
        setDebugResponse(data);
        
        if (response.ok && data.success) {
            displayAdminSpecialists(data.data);
        } else {
            showError(extractErrorMessage(data, response));
        }
    } catch (error) {
        setDebugError(error.message);
        showError('Error loading specialists: ' + error.message);
    }
}

// Display admin specialists
function displayAdminSpecialists(specialists) {
    var list = document.getElementById('admin-specialists-list');
    
    if (!specialists || specialists.length === 0) {
        list.innerHTML = '<p>No specialists found.</p>';
        return;
    }
    
    var html = '<table><thead><tr>';
    html += '<th>ID</th><th>User</th><th>Category</th><th>Level</th><th>Status</th><th>Fee</th><th>Information</th><th>Action</th>';
    html += '</tr></thead><tbody>';
    
    specialists.forEach(function(sp) {
        html += '<tr>';
        html += '<td>' + sp.specialistId + '</td>';
        html += '<td>' + (sp.username || '-') + '</td>';
        html += '<td>' + (sp.categoryName || '-') + '</td>';
        html += '<td>' + (sp.levelName || '-') + '</td>';
        html += '<td>' + sp.status + '</td>';
        html += '<td>' + (sp.fee || '-') + '</td>';
        html += '<td>' + (sp.information || '-') + '</td>';
        var actionBtn = sp.status === 'ACTIVE'
            ? '<button onclick="deactivateSpecialistAdmin(' + sp.specialistId + ')">Deactivate</button>'
            : '<button disabled>Inactive</button>';
        html += '<td>' + actionBtn + '</td>';
        html += '</tr>';
    });
    
    html += '</tbody></table>';
    list.innerHTML = html;
}

// Create specialist
async function handleCreateSpecialist(e) {
    e.preventDefault();
    
    var userId = parseInt(document.getElementById('sp-user-id').value);
    var categoryId = parseInt(document.getElementById('sp-category-id').value);
    var levelId = parseInt(document.getElementById('sp-level-id').value);
    var fee = parseFloat(document.getElementById('sp-fee').value);
    var status = document.getElementById('sp-status').value;
    var information = document.getElementById('sp-information').value;
    
    var requestBody = { userId: userId, categoryId: categoryId, levelId: levelId, fee: fee, status: status, information: information };
    
    clearDebug();
    setDebugMethod('POST');
    setDebugUrl('/api/specialists');
    setDebugRequest(requestBody);
    
    try {
        var response = await fetch('/api/specialists', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(requestBody)
        });
        
        var data = await response.json();
        setDebugResponse(data);
        
        if (response.ok && data.success) {
            showMessage('Specialist created successfully');
            document.getElementById('create-specialist-form').reset();
            loadAllSpecialistsAdmin();
        } else {
            showError(extractErrorMessage(data, response));
        }
    } catch (error) {
        setDebugError(error.message);
        showError('Error creating specialist: ' + error.message);
    }
}

// Update specialist
async function handleUpdateSpecialist(e) {
    e.preventDefault();
    
    var specialistId = parseInt(document.getElementById('update-sp-id').value);
    var categoryId = document.getElementById('update-sp-cat').value;
    var levelId = document.getElementById('update-sp-level').value;
    var fee = document.getElementById('update-sp-fee').value;
    var status = document.getElementById('update-sp-status').value;
    var information = document.getElementById('update-sp-info').value;
    
    var requestBody = {};
    if (categoryId) requestBody.categoryId = parseInt(categoryId);
    if (levelId) requestBody.levelId = parseInt(levelId);
    if (fee) requestBody.fee = parseFloat(fee);
    if (status) requestBody.status = status;
    if (information) requestBody.information = information;
    
    clearDebug();
    setDebugMethod('PUT');
    setDebugUrl('/api/specialists/' + specialistId);
    setDebugRequest(requestBody);
    
    try {
        var response = await fetch('/api/specialists/' + specialistId, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(requestBody)
        });
        
        var data = await response.json();
        setDebugResponse(data);
        
        if (response.ok && data.success) {
            showMessage('Specialist updated successfully');
            document.getElementById('update-specialist-form').reset();
            loadAllSpecialistsAdmin();
        } else {
            showError(extractErrorMessage(data, response));
        }
    } catch (error) {
        setDebugError(error.message);
        showError('Error updating specialist: ' + error.message);
    }
}

// Deactivate specialist (admin)
async function deactivateSpecialistAdmin(specialistId) {
    if (!confirm('Are you sure you want to deactivate this specialist?')) {
        return;
    }
    
    clearDebug();
    setDebugMethod('PUT');
    setDebugUrl('/api/specialists/' + specialistId);
    setDebugRequest({ status: 'INACTIVE' });
    
    try {
        var response = await fetch('/api/specialists/' + specialistId, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ status: 'INACTIVE' })
        });
        
        var data = await response.json();
        setDebugResponse(data);
        
        if (response.ok && data.success) {
            showMessage('Specialist deactivated');
            loadAllSpecialistsAdmin();
        } else {
            showError(extractErrorMessage(data, response));
        }
    } catch (error) {
        setDebugError(error.message);
        showError('Error deactivating specialist: ' + error.message);
    }
}

// ============= SLOT MANAGEMENT =============

// Load slots by specialist
async function loadSlotsBySpecialist() {
    var specialistId = document.getElementById('slot-specialist-id').value;
    if (!specialistId) {
        showError('Please enter a specialist ID');
        return;
    }
    
    clearDebug();
    setDebugMethod('GET');
    setDebugUrl('/api/slots/specialist/' + specialistId);
    
    try {
        var response = await fetch('/api/slots/specialist/' + specialistId);
        var data = await response.json();
        setDebugResponse(data);
        
        if (response.ok && data.success) {
            displaySlots(data.data);
        } else {
            showError(extractErrorMessage(data, response));
        }
    } catch (error) {
        setDebugError(error.message);
        showError('Error loading slots: ' + error.message);
    }
}

// Load available slots by specialist
async function loadAvailableSlotsBySpecialist() {
    var specialistId = document.getElementById('slot-specialist-id').value;
    if (!specialistId) {
        showError('Please enter a specialist ID');
        return;
    }
    
    clearDebug();
    setDebugMethod('GET');
    setDebugUrl('/api/slots/specialist/' + specialistId + '/available');
    
    try {
        var response = await fetch('/api/slots/specialist/' + specialistId + '/available');
        var data = await response.json();
        setDebugResponse(data);
        
        if (response.ok && data.success) {
            displaySlots(data.data);
        } else {
            showError(extractErrorMessage(data, response));
        }
    } catch (error) {
        setDebugError(error.message);
        showError('Error loading slots: ' + error.message);
    }
}

// Display slots
function displaySlots(slots) {
    var list = document.getElementById('slots-list');
    
    if (!slots || slots.length === 0) {
        list.innerHTML = '<p>No slots found.</p>';
        return;
    }
    
    var html = '<table><thead><tr>';
    html += '<th>ID</th><th>Specialist</th><th>Day</th><th>Start</th><th>End</th><th>Status</th><th>Action</th>';
    html += '</tr></thead><tbody>';
    
    slots.forEach(function(slot) {
        html += '<tr>';
        html += '<td>' + slot.slotId + '</td>';
        html += '<td>' + (slot.specialistName || '-') + '</td>';
        html += '<td>' + (slot.dayOfWeek || '-') + '</td>';
        html += '<td>' + formatTime(slot.startTime) + '</td>';
        html += '<td>' + formatTime(slot.endTime) + '</td>';
        html += '<td>' + slot.status + '</td>';
        var actionBtn = slot.status === 'AVAILABLE'
            ? '<button onclick="deleteSlot(' + slot.slotId + ')">Delete</button>'
            : '<button disabled>' + slot.status + '</button>';
        html += '<td>' + actionBtn + '</td>';
        html += '</tr>';
    });
    
    html += '</tbody></table>';
    list.innerHTML = html;
}

// Create slot
async function handleCreateSlot(e) {
    e.preventDefault();
    
    var specialistId = parseInt(document.getElementById('slot-specialist-id-create').value);
    var dayOfWeek = document.getElementById('slot-day-of-week').value;
    var startTime = document.getElementById('slot-start').value;
    var endTime = document.getElementById('slot-end').value;
    var status = document.getElementById('slot-status').value;
    
    var requestBody = { specialistId: specialistId, dayOfWeek: dayOfWeek, startTime: startTime, endTime: endTime };
    if (status) requestBody.status = status;
    
    clearDebug();
    setDebugMethod('POST');
    setDebugUrl('/api/slots');
    setDebugRequest(requestBody);
    
    try {
        var response = await fetch('/api/slots', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(requestBody)
        });
        
        var data = await response.json();
        setDebugResponse(data);
        
        if (response.ok && data.success) {
            showMessage('Slot created successfully');
            document.getElementById('create-slot-form').reset();
        } else {
            showError(extractErrorMessage(data, response));
        }
    } catch (error) {
        setDebugError(error.message);
        showError('Error creating slot: ' + error.message);
    }
}

// Update slot
async function handleUpdateSlot(e) {
    e.preventDefault();
    
    var slotId = parseInt(document.getElementById('update-slot-id').value);
    var specialistId = document.getElementById('update-slot-specialist').value;
    var dayOfWeek = document.getElementById('update-slot-day-of-week').value;
    var startTime = document.getElementById('update-slot-start').value;
    var endTime = document.getElementById('update-slot-end').value;
    var status = document.getElementById('update-slot-status').value;
    
    var requestBody = {};
    if (specialistId) requestBody.specialistId = parseInt(specialistId);
    if (dayOfWeek) requestBody.dayOfWeek = dayOfWeek;
    if (startTime) requestBody.startTime = startTime;
    if (endTime) requestBody.endTime = endTime;
    if (status) requestBody.status = status;
    
    clearDebug();
    setDebugMethod('PUT');
    setDebugUrl('/api/slots/' + slotId);
    setDebugRequest(requestBody);
    
    try {
        var response = await fetch('/api/slots/' + slotId, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(requestBody)
        });
        
        var data = await response.json();
        setDebugResponse(data);
        
        if (response.ok && data.success) {
            showMessage('Slot updated successfully');
            document.getElementById('update-slot-form').reset();
        } else {
            showError(extractErrorMessage(data, response));
        }
    } catch (error) {
        setDebugError(error.message);
        showError('Error updating slot: ' + error.message);
    }
}

// Delete slot
async function deleteSlot(slotId) {
    if (!confirm('Are you sure you want to delete this slot?')) {
        return;
    }
    
    clearDebug();
    setDebugMethod('DELETE');
    setDebugUrl('/api/slots/' + slotId);
    
    try {
        var response = await fetch('/api/slots/' + slotId, {
            method: 'DELETE'
        });
        
        var data = await response.json();
        setDebugResponse(data);
        
        if (response.ok && data.success) {
            showMessage('Slot deleted');
            loadSlotsBySpecialist();
        } else {
            showError(extractErrorMessage(data, response));
        }
    } catch (error) {
        setDebugError(error.message);
        showError('Error deleting slot: ' + error.message);
    }
}

// Load levels
async function loadLevels() {
    clearDebug();
    setDebugMethod('GET');
    setDebugUrl('/api/admin/levels');
    
    try {
        var response = await fetch('/api/admin/levels');
        var data = await response.json();
        setDebugResponse(data);
        
        if (response.ok && data.success) {
            displayLevels(data.data);
        } else {
            showError(extractErrorMessage(data, response));
        }
    } catch (error) {
        setDebugError(error.message);
        showError('Error loading levels: ' + error.message);
    }
}

// Display levels
function displayLevels(levels) {
    var list = document.getElementById('levels-list');
    
    if (!levels || levels.length === 0) {
        list.innerHTML = '<p>No levels found.</p>';
        return;
    }
    
    var html = '<table><thead><tr>';
    html += '<th>ID</th><th>Name</th><th>Status</th>';
    html += '</tr></thead><tbody>';
    
    levels.forEach(function(lvl) {
        html += '<tr>';
        html += '<td>' + lvl.levelId + '</td>';
        html += '<td>' + (lvl.name || '-') + '</td>';
        html += '<td>' + (lvl.status || '-') + '</td>';
        html += '</tr>';
    });
    
    html += '</tbody></table>';
    list.innerHTML = html;
}

// ============= DEBUG & MESSAGE HELPERS =============

function clearDebug() {
    setDebugMethod('');
    setDebugUrl('');
    setDebugRequest({});
    setDebugResponse({});
    setDebugError('');
}

function setDebugUser(user) {
    document.getElementById('debug-user').textContent = JSON.stringify(user, null, 2);
}

function setDebugSpecialist(specialist) {
    document.getElementById('debug-specialist').textContent = specialist ? JSON.stringify(specialist, null, 2) : 'null';
}

function setDebugSlot(slot) {
    document.getElementById('debug-slot').textContent = slot ? JSON.stringify(slot, null, 2) : 'null';
}

function setDebugBooking(booking) {
    document.getElementById('debug-booking').textContent = booking ? JSON.stringify(booking, null, 2) : 'null';
}

function setDebugMethod(method) {
    document.getElementById('debug-method').textContent = method;
}

function setDebugUrl(url) {
    document.getElementById('debug-url').textContent = url;
}

function setDebugRequest(request) {
    document.getElementById('debug-request').textContent = JSON.stringify(request, null, 2);
}

function setDebugResponse(response) {
    document.getElementById('debug-response').textContent = JSON.stringify(response, null, 2);
}

function setDebugError(error) {
    document.getElementById('debug-error').textContent = error;
}

// Toggle debug panel visibility
function toggleDebugPanel() {
    var content = document.getElementById('debug-content');
    var toggleText = document.getElementById('debug-toggle-text');
    
    if (content.classList.contains('hidden')) {
        content.classList.remove('hidden');
        toggleText.textContent = 'Hide Debug Info';
    } else {
        content.classList.add('hidden');
        toggleText.textContent = 'Show Debug Info';
    }
}

// Cancel reschedule
function cancelReschedule() {
    document.getElementById('reschedule-section').classList.add('hidden');
    selectedBooking = null;
    setDebugBooking(null);
}

function showMessage(message) {
    var messages = document.getElementById('messages');
    var div = document.createElement('div');
    div.className = 'message';
    div.textContent = message;
    messages.appendChild(div);
    
    setTimeout(function() {
        div.remove();
    }, 5000);
}

function showError(message) {
    var messages = document.getElementById('messages');
    var div = document.createElement('div');
    div.className = 'message error';
    div.textContent = message;
    messages.appendChild(div);
    
    setTimeout(function() {
        div.remove();
    }, 8000);
}
