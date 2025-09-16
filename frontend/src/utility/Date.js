function addDays(startDate, days) {
  const newDate = new Date(startDate);
  newDate.setDate(startDate.getDate() + days);
  return newDate;
}

function daysBetween(startDate, endDate) {
  const ms = 24 * 60 * 60 * 1000;

  return Math.round(Math.abs((new Date(startDate) - new Date(endDate)) / ms));
}

function formatDate(date) {
  return date.toISOString().split("T")[0];
}

export { addDays, daysBetween, formatDate };
