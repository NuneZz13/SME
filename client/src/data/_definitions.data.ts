interface IValidation {
  fieldName: string,
  nrValidations: number,
}

enum CSValidationEnum {
  CLIENT = 'client',
  SERVER = 'server',
}

interface ICSValidation {
  title: CSValidationEnum,
  validations: IValidation[],
}

interface IFormData {
  fields: string[],
  csValidations: ICSValidation[],
}


interface IKFormServerSideValidationsData {
  path: string,
  validation: {
    name: string,
    descr: string,
  }
}

interface IKFormSharedValidationsData {
  scopes: string[],
  scopeValidations: {
    scope: string,
    validations: {
      field: string,
      rules: string[]
    }[],
  }[]
}


export { CSValidationEnum };
export type {IFormData, IKFormSharedValidationsData, IKFormServerSideValidationsData};
