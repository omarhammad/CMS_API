async function getOneDoctor(doctor_id) {
    try {
        const response = await fetch(`http://localhost:8080/api/doctors/${doctor_id}`);
        if (response.status === 404) {
            console.log("NOT FOUND");
        }
        return await response.json();
    } catch (exc) {
        console.error(exc);
        return null;
    }
}


window.addEventListener('DOMContentLoaded', async event => { // Add async keyword here
    const doctor_id = new URLSearchParams(window.location.search).get('doctor_id');
    const data = await getOneDoctor(doctor_id);
    if (data) {
        document.getElementById('fullName').innerText = data.firstName + ' ' + data.lastName;
        document.getElementById('specialization').innerText = data.specialization;
        const contactInfo = data.contactInfo.split(',');
        document.getElementById('phone').innerText = contactInfo[0]
        document.getElementById('email').innerText = contactInfo[1]
        const hisPatients = data.hisPatients;
        if (hisPatients.length !== 0) {
            for (const patient of hisPatients) {
                const p = document.createElement('p');
                p.classList.add("d-block", "details-text");
                p.innerText = patient.firstName + ' ' + patient.lastName;
                document.getElementById('upcoming-appointments').appendChild(p);
            }
        } else {
            const p = document.createElement('p');
            p.classList.add("h4", "m-3");
            p.innerText = "There is No appointments";
            document.getElementById('upcoming-appointments').appendChild(p);

        }


    } else {
        console.log('No data returned for this doctor.');
    }
});
