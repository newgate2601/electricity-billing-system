const register = (event) => {
    event.preventDefault();

    const formData = new FormData(event.target);
    const body = Object.fromEntries(formData.entries());

    fetch('http://localhost:8080/auth/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(body)
    })
        .then(response => {
            if (response.ok) {
                location.href = "/auth/login"
            } else {
                throw new Error('Registration failed');
            }
        })
        .catch(error => console.error(error));
};

document.getElementById('registerForm').addEventListener('submit', register);