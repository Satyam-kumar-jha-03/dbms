document.addEventListener('DOMContentLoaded', function() {
    // Initialize DataTable
    const doctorsTable = $('#doctors-table').DataTable({
        ajax: {
            url: 'http://localhost:8080/api/doctors',
            dataSrc: ''
        },
        columns: [
            { data: 'doctor_id' },
            { 
                data: null,
                render: function(data, type, row) {
                    return `${row.F_name} ${row.L_name}`;
                }
            },
            { data: 'specialist' },
            { data: 'age' },
            { data: 'gender' },
            { data: 'hospital_id' },
            {
                data: null,
                render: function(data, type, row) {
                    return `
                        <div class="action-buttons">
                            <button class="btn btn-sm btn-edit" data-id="${row.doctor_id}">
                                <i class="fas fa-edit"></i>
                            </button>
                            <button class="btn btn-sm btn-delete" data-id="${row.doctor_id}">
                                <i class="fas fa-trash"></i>
                            </button>
                            <button class="btn btn-sm btn-view" data-id="${row.doctor_id}">
                                <i class="fas fa-eye"></i>
                            </button>
                        </div>
                    `;
                }
            }
        ],
        responsive: true
    });

    // Modal handling
    const modal = document.getElementById('add-doctor-modal');
    const addBtn = document.getElementById('add-doctor-btn');
    const closeBtns = document.querySelectorAll('.close-modal');

    addBtn.addEventListener('click', () => {
        modal.style.display = 'block';
    });

    closeBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            modal.style.display = 'none';
        });
    });

    window.addEventListener('click', (e) => {
        if (e.target === modal) {
            modal.style.display = 'none';
        }
    });

    // Form submission
    const doctorForm = document.getElementById('doctor-form');
    doctorForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const doctorData = {
            F_name: document.getElementById('first-name').value,
            L_name: document.getElementById('last-name').value,
            age: document.getElementById('age').value,
            specialist: document.getElementById('specialization').value,
            gender: document.getElementById('gender').value,
            doctor_id: document.getElementById('doctor-id').value,
            hospital_id: document.getElementById('hospital-id').value
        };

        try {
            const response = await fetch('http://localhost:8080/api/doctors', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(doctorData)
            });

            if (response.ok) {
                doctorsTable.ajax.reload();
                modal.style.display = 'none';
                doctorForm.reset();
                alert('Doctor added successfully!');
            } else {
                throw new Error('Failed to add doctor');
            }
        } catch (error) {
            console.error('Error:', error);
            alert('Error adding doctor. Please try again.');
        }
    });

    // Search functionality
    const searchInput = document.getElementById('doctor-search');
    searchInput.addEventListener('keyup', () => {
        doctorsTable.search(searchInput.value).draw();
    });
});