import { HttpStatus } from "./httpStatus.js"

export async function getCurrentUser() {
  const response = await fetch("http://localhost:8080/api/auth/user/current")

  if (response.status === HttpStatus.UNAUTHORIZED) {
    window.location.href = "/signIn"
  } else if (response.status === HttpStatus.OK) {
    return await response.json()
  }
}
