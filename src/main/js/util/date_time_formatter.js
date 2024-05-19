export function formatTime(date) {
  let hours = date.getHours()
  const minutes = date.getMinutes()
  const ampm = hours >= 12 ? "PM" : "AM"

  hours %= 12
  hours = hours || 12 // the hour '0' should be '12 '

  const minutesFormatted = minutes < 10 ? "0" + minutes : minutes
  const hoursFormatted = hours < 10 ? "0" + hours : hours

  return `${hoursFormatted}:${minutesFormatted} ${ampm}`
}

export function formatDate(date) {
  const monthNames = [
    "Jan",
    "Feb",
    "Mar",
    "Apr",
    "May",
    "Jun",
    "Jul",
    "Aug",
    "Sep",
    "Oct",
    "Nov",
    "Dec",
  ]
  const day = date.getDate()
  const monthIndex = date.getMonth()
  const year = date.getFullYear()

  return `${monthNames[monthIndex]} ${day}, ${year}`
}
