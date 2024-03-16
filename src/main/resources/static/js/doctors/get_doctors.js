window.addEventListener('DOMContentLoaded', getAllDoctors);

function getAllDoctors() {
    const doctorsBody = document.querySelector('.doctorsTbody');
    fetch('http://localhost:8080/api/doctors')
        .then(response => {
            if (response.status === 204) {
                let noContentRow = document.createElement('tr');
                let noContentCell = document.createElement('td');
                noContentCell.innerHTML = "NO DOCTORS !";
                noContentCell.colSpan = 7;
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

                // DELETE BUTTON
                const deleteBtn = document.createElement('td');
                deleteBtn.className = "bi bi-trash-fill text-danger";
                deleteBtn.addEventListener('click', event => {
                    event.stopPropagation();
                    deleteDoctor(doctor.id)
                })

                //EDIT BUTTON
                const editBtn = document.createElement('td');
                editBtn.className = "bi bi-pencil-fill text-primary"
                editBtn.addEventListener('click', event => {
                    event.stopPropagation();
                    window.location.href = `/doctors/update/${doctor.id}`;

                })


                doctor_row.appendChild(deleteBtn)
                doctor_row.appendChild(editBtn)
                doctor_row.addEventListener('click', () => {
                        window.location.href = `/doctors/details/${doctor.id}`;
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
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    const headers = new Headers({
        [csrfHeader]: csrfToken
    });
    fetch(`http://localhost:8080/api/doctors/${delete_doctor_id}`, {
        method: 'DELETE',
        headers: headers
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




