import { CSValidationEnum, IFormData } from './_definitions.data';

const classicData: IFormData = {
  fields: [
    'name',
    'email',
    'phoneNumber',
    'birthDate',
  ],
  csValidations: [
    {
      title: CSValidationEnum.CLIENT,
      validations: [
        { fieldName: 'name', nrValidations: 1 },
        { fieldName: 'phoneNumber', nrValidations: 2 },
      ],
    },
    {
      title: CSValidationEnum.SERVER,
      validations: [
        { fieldName: 'name', nrValidations: 1 },
        { fieldName: 'email', nrValidations: 1 },
        { fieldName: 'phoneNumber', nrValidations: 2 },
        { fieldName: 'birthDate', nrValidations: 1 },
      ],
    },
  ],
};

export default classicData;
