// GET ALL Doctor
async function getAllDoctors() {
    try {
        const response = await fetch('https://localhost:8080/api/doctors/');
        if (response.status === 204) {
            return "NO CONTENT";
        }
        return await response.json();
    } catch (exc) {
        return exc.message
    }
}

// GET 1 Doctor
async function getOneDoctor(get_doctor_id) {
    try {
        const response = await fetch(`https://localhost:8080/api/doctors/${get_doctor_id}`);
        if (response.status === 404) {
            console.log("NOT FOUND");
            return;
        }
        return await response.json();
    } catch (exc) {
        return exc.message
    }

}


// DELETE
delete_doctor_id = 11

function deleteDoctor(delete_doctor_id) {
    fetch(`https://localhost:8080/api/doctors/${delete_doctor_id}`, {
        method: 'DELETE'
    })
        .then(response => {
            if (response.status === 404) {
                console.log("NOT FOUND");
            } else if (response.status === 204) {
                console.log("DOCTOR DELETED!");
            }
        }).catch(err => {
        console.error(err);
    });
}

// POST A DOCTOR

/*
{
  "firstName": "Omar",
  "lastName": "Hammad",
  "specialization": "Urology",
  "phoneNumber": "+32465358794",
  "email": "omar.hammad@student.kd.be"
}
* */
function createDoctor(doctorJson) {
    fetch('https://localhost:8080/api/doctors/',
        {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: doctorJson
        })
        .then(response => {
            const data = response.json();
            if (response.status === 400) {
                if (data.hasOwnProperty('exceptionMsg')) {
                    console.log(`Status code is ${response.status}`)
                    console.log(data.exceptionMsg);
                    return
                } else if (data.hasOwnProperty('error')) {
                    console.log(`Status code is ${response.status}`)
                    console.log(data);
                    return;
                }
            }
            return response.json();
        })
        .then(data => {
            console.log(`Status code is ${200}`)
            console.log(data)
        }).catch(err => {
        console.error(err);
    });
}