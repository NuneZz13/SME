export interface ErrorResponseData<T> {
  fieldError: keyof T,
  msg: string,
}

export interface SuccessfulResponseData {
  redirectUrl: string,
  msg: string,
}



export const isEmpty = (value: string) => {
  return !value || value.trim() === ''
}


export const validPhoneNumber = {
  startsWith2Or9: (value: string) => startsWith2Or9(value),
  expected9Digits: (value: string) => expected9Digits(value),
}

const startsWith2Or9 = (value: string) => {
  return value.startsWith('2') || value.startsWith('9')
}

const expected9Digits = (value: string) => {
  return value.length == 9
}

