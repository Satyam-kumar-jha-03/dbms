// Sample data - In a real app, this would come from your backend API
const sampleData = {
    doctors: 24,
    patients: 156,
    hospitals: 5,
    records: 342,
    activities: [
        {
            type: 'doctor',
            action: 'added',
            name: 'Dr. Sarah Johnson',
            time: '10 mins ago'
        },
        {
            type: 'patient',
            action: 'updated',
            name: 'Michael Brown',
            time: '25 mins ago'
        },
        {
            type: 'record',
            action: 'created',
            name: 'Record #R-2023-0456',
            time: '1 hour ago'
        },
        {
            type: 'hospital',
            action: 'added',
            name: 'City General Hospital',
            time: '2 hours ago'
        },
        {
            type: 'relative',
            action: 'registered',
            name: 'Emily Wilson',
            time: '3 hours ago'
        }
    ]
};

// DOM Content Loaded
document.addEventListener('DOMContentLoaded', function() {
    // Update dashboard counts
    document.getElementById('doctor-count').textContent = sampleData.doctors;
    document.getElementById('patient-count').textContent = sampleData.patients;
    document.getElementById('hospital-count').textContent = sampleData.hospitals;
    document.getElementById('record-count').textContent = sampleData.records;

    // Populate recent activities
    const activityList = document.getElementById('recent-activity');
    
    sampleData.activities.forEach(activity => {
        const activityItem = document.createElement('div');
        activityItem.className = 'activity-item';
        
        let iconClass = '';
        let iconBg = '';
        
        switch(activity.type) {
            case 'doctor':
                iconClass = 'fas fa-user-md';
                iconBg = 'bg-blue';
                break;
            case 'patient':
                iconClass = 'fas fa-procedures';
                iconBg = 'bg-green';
                break;
            case 'hospital':
                iconClass = 'fas fa-hospital-alt';
                iconBg = 'bg-orange';
                break;
            case 'record':
                iconClass = 'fas fa-file-medical';
                iconBg = 'bg-purple';
                break;
            case 'relative':
                iconClass = 'fas fa-users';
                iconBg = 'bg-dark';
                break;
        }
        
        activityItem.innerHTML = `
            <div class="activity-icon ${iconBg}">
                <i class="${iconClass}"></i>
            </div>
            <div class="activity-details">
                <p><strong>${activity.name}</strong> was ${activity.action}</p>
                <span class="activity-time">${activity.time}</span>
            </div>
        `;
        
        activityList.appendChild(activityItem);
    });
});

// API Integration Functions (to be implemented with your backend)
async function fetchDoctors() {
    try {
        const response = await fetch('http://localhost:8080/api/doctors');
        return await response.json();
    } catch (error) {
        console.error('Error fetching doctors:', error);
        return [];
    }
}

async function fetchPatients() {
    try {
        const response = await fetch('http://localhost:8080/api/patients');
        return await response.json();
    } catch (error) {
        console.error('Error fetching patients:', error);
        return [];
    }
}

async function fetchHospitals() {
    try {
        const response = await fetch('http://localhost:8080/api/hospitals');
        return await response.json();
    } catch (error) {
        console.error('Error fetching hospitals:', error);
        return [];
    }
}

async function fetchRecords() {
    try {
        const response = await fetch('http://localhost:8080/api/records');
        return await response.json();
    } catch (error) {
        console.error('Error fetching records:', error);
        return [];
    }
}

async function fetchRelatives() {
    try {
        const response = await fetch('http://localhost:8080/api/relatives');
        return await response.json();
    } catch (error) {
        console.error('Error fetching relatives:', error);
        return [];
    }
}