
export const GET_HEADER = {
  method: 'get',
  headers: {
    Accept: 'application/json',
    'Content-Type': 'application/json'
  }
}

export const POST_HEADER = (csrfToken) => {
  return {
    Accept: 'application/json',
    'csrf-token': csrfToken,
    'x-xsrf-token': csrfToken,
    'Content-Type': 'application/json'
  }

}
