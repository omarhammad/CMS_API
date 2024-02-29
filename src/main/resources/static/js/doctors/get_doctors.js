window.addEventListener('DOMContentLoaded', getAllDoctors);

function getAllDoctors() {
    const doctorsBody = document.querySelector('.doctorsTbody');
    fetch('http://localhost:8080/api/doctors')
        .then(response => {
            if (response.status === 204) {
                let noContentRow = document.createElement('tr');
                let noContentCell = document.createElement('td');
                noContentCell.innerHTML = "NO DOCTORS !";
                noContentCell.colSpan = 6;
                noContentRow.appendChild(noContentCell);
                doctorsBody.appendChild(noContentRow);
            } else if (response.ok) {
                return response.json();
            } else {
                console.error("Failed to fetch doctors:", response.statusText);
            }
        })
        .then(doctors => {
            for (const doctor of doctors) {
                let doctor_row = document.createElement('tr');
                for (const key in doctor) {

                    if (key === 'id') {
                        continue;
                    } else if (key === 'contactInfo') {
                        const contactInfo = doctor[key].split(',');
                        const phone = document.createElement('td');
                        const email = document.createElement('td');
                        phone.innerText = contactInfo[0];
                        email.innerText = contactInfo[1];
                        doctor_row.appendChild(phone);
                        doctor_row.appendChild(email);
                        continue;
                    }
                    let cell = document.createElement('td');
                    cell.innerText = doctor[key];
                    doctor_row.appendChild(cell);
                }
                const deleteBtn = document.createElement('td');
                deleteBtn.className = "bi bi-trash-fill text-danger";
                deleteBtn.addEventListener('click', event => {
                    deleteDoctor(doctor.id)
                })
                doctor_row.appendChild(deleteBtn)
                doctor_row.addEventListener('click',() => {
                        window.location.href = `doctor_details_page.html?doctor_id=${doctor.id}`
                    }
                );
                doctorsBody.appendChild(doctor_row);
            }
        })
        .catch(err => {
            console.error("Fetching doctors failed:", err);

        });
}

function deleteDoctor(delete_doctor_id) {
    fetch(`http://localhost:8080/api/doctors/${delete_doctor_id}`, {
        method: 'DELETE'
    })
        .then(response => {
            if (response.status === 404) {
                console.log("NOT FOUND");
            } else if (response.status === 204) {
                console.log("DOCTOR DELETED!");
                document.querySelector('.doctorsTbody').innerHTML = '';

                getAllDoctors();
            }
        }).catch(err => {
        console.error('Omar', err);
    });
}






