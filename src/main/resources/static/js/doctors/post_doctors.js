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