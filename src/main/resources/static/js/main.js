/* ====================================
   FoodShare - Main JavaScript
   Interactive Features & Animations
   ==================================== */

// ========== DARK MODE TOGGLE ==========
function toggleDarkMode() {
    const body = document.body;
    body.classList.toggle('dark-mode');
    const isDark = body.classList.contains('dark-mode');
    localStorage.setItem('darkMode', isDark);
    
    // Update icon
    const icon = document.querySelector('.dark-mode-toggle i');
    if (icon) {
        icon.className = isDark ? 'fas fa-sun' : 'fas fa-moon';
    }
    
    // Show toast notification
    showToast(isDark ? 'Dark mode enabled' : 'Light mode enabled', 'success');
}

// Restore dark mode on page load
window.addEventListener('DOMContentLoaded', function() {
    const darkMode = localStorage.getItem('darkMode');
    if (darkMode === 'true') {
        document.body.classList.add('dark-mode');
        const icon = document.querySelector('.dark-mode-toggle i');
        if (icon) {
            icon.className = 'fas fa-sun';
        }
    }
    
    // Initialize animations
    initAnimations();
});

// ========== TOAST NOTIFICATIONS ==========
function showToast(message, type = 'info') {
    // Remove existing toasts
    const existingToast = document.querySelector('.custom-toast');
    if (existingToast) {
        existingToast.remove();
    }
    
    // Create toast element
    const toast = document.createElement('div');
    toast.className = `custom-toast glass-toast fade-in ${type}`;
    toast.innerHTML = `
        <div class="d-flex align-items-center">
            <i class="fas ${getToastIcon(type)} me-3"></i>
            <span>${message}</span>
            <button type="button" class="btn-close ms-auto" onclick="this.parentElement.parentElement.remove()"></button>
        </div>
    `;
    
    // Style toast
    toast.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 1rem 1.5rem;
        z-index: 9999;
        min-width: 300px;
        max-width: 500px;
    `;
    
    document.body.appendChild(toast);
    
    // Auto remove after 4 seconds
    setTimeout(() => {
        toast.style.opacity = '0';
        setTimeout(() => toast.remove(), 300);
    }, 4000);
}

function getToastIcon(type) {
    const icons = {
        'success': 'fa-check-circle',
        'error': 'fa-exclamation-circle',
        'warning': 'fa-exclamation-triangle',
        'info': 'fa-info-circle'
    };
    return icons[type] || icons.info;
}

// ========== FORM VALIDATION ==========
function validateForm(formId) {
    const form = document.getElementById(formId);
    if (!form) return false;
    
    const inputs = form.querySelectorAll('input[required], textarea[required], select[required]');
    let isValid = true;
    
    inputs.forEach(input => {
        if (!input.value.trim()) {
            isValid = false;
            input.classList.add('is-invalid');
            input.addEventListener('input', () => input.classList.remove('is-invalid'), { once: true });
        }
    });
    
    if (!isValid) {
        showToast('Please fill in all required fields', 'error');
    }
    
    return isValid;
}

// ========== IMAGE PREVIEW ==========
function previewImage(input, previewId) {
    if (input.files && input.files[0]) {
        const reader = new FileReader();
        reader.onload = function(e) {
            const preview = document.getElementById(previewId);
            if (preview) {
                preview.src = e.target.result;
                preview.style.display = 'block';
                preview.classList.add('fade-in');
            }
        };
        reader.readAsDataURL(input.files[0]);
    }
}

// ========== ANIMATIONS ==========
function initAnimations() {
    // Fade in elements on scroll
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    };
    
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('fade-in');
                observer.unobserve(entry.target);
            }
        });
    }, observerOptions);
    
    // Observe all cards
    document.querySelectorAll('.glass-card, .donation-card, .glass-stat-card').forEach(el => {
        observer.observe(el);
    });
}

// ========== CONFIRMATION DIALOG ==========
function confirmAction(message, callback) {
    const confirmed = confirm(message);
    if (confirmed && typeof callback === 'function') {
        callback();
    }
    return confirmed;
}

// ========== DELETE DONATION ==========
function deleteDonation(donationId) {
    confirmAction('Are you sure you want to delete this donation?', () => {
        // Make AJAX request or form submission
        window.location.href = `/donor/delete/${donationId}`;
    });
}

// ========== CLAIM DONATION ==========
function claimDonation(donationId) {
    confirmAction('Are you sure you want to claim this food donation?', () => {
        // Make AJAX request or form submission
        const form = document.createElement('form');
        form.method = 'POST';
        form.action = `/receiver/claim/${donationId}`;
        document.body.appendChild(form);
        form.submit();
    });
}

// ========== SEARCH & FILTER ==========
function filterDonations(searchTerm, filterType) {
    const cards = document.querySelectorAll('.donation-card');
    let visibleCount = 0;
    
    cards.forEach(card => {
        const name = card.querySelector('.food-name')?.textContent.toLowerCase() || '';
        const type = card.querySelector('.food-type')?.textContent.toLowerCase() || '';
        const city = card.querySelector('.food-city')?.textContent.toLowerCase() || '';
        
        const matchesSearch = name.includes(searchTerm.toLowerCase()) || 
                             city.includes(searchTerm.toLowerCase());
        const matchesFilter = !filterType || type.includes(filterType.toLowerCase());
        
        if (matchesSearch && matchesFilter) {
            card.style.display = 'block';
            visibleCount++;
        } else {
            card.style.display = 'none';
        }
    });
    
    // Show no results message
    const noResults = document.getElementById('no-results');
    if (noResults) {
        noResults.style.display = visibleCount === 0 ? 'block' : 'none';
    }
}

// ========== CHART INITIALIZATION ==========
function initChart(canvasId, type, data, options = {}) {
    const ctx = document.getElementById(canvasId);
    if (!ctx) return null;
    
    const defaultOptions = {
        responsive: true,
        maintainAspectRatio: true,
        plugins: {
            legend: {
                position: 'bottom',
                labels: {
                    font: {
                        family: 'Poppins',
                        size: 12
                    },
                    padding: 15
                }
            }
        }
    };
    
    return new Chart(ctx, {
        type: type,
        data: data,
        options: { ...defaultOptions, ...options }
    });
}

// ========== AUTO-HIDE ALERTS ==========
window.addEventListener('DOMContentLoaded', function() {
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.opacity = '0';
            setTimeout(() => alert.remove(), 300);
        }, 5000);
    });
});

// ========== SMOOTH SCROLL ==========
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
        const href = this.getAttribute('href');
        if (href !== '#') {
            e.preventDefault();
            const target = document.querySelector(href);
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        }
    });
});

// ========== EXPORT FUNCTIONS ==========
window.FoodShare = {
    toggleDarkMode,
    showToast,
    validateForm,
    previewImage,
    deleteDonation,
    claimDonation,
    filterDonations,
    initChart,
    confirmAction
};
