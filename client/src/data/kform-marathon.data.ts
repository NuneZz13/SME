import {IKFormServerSideValidationsData, IKFormSharedValidationsData} from './_definitions.data';


const serverSideValidations: IKFormServerSideValidationsData[] = [
  {
    path: '/personalInfo/email',
    validation: {
      name: 'AllowedEmailDomain',
      descr: 'Invalid domains: "exemplo.com", "example.com", "test.com", "gmial.com", "gmali.com", "hotmial.com", "hotmali.com", "outlok.com"',
    },
  },
  {
    path: '/personalInfo/phoneNumber',
    validation: {
      name: 'RequiredPhoneNumberWhenEmailDomainIsCommon',
      descr: 'If email domain is "gmail.com" OR "hotmail.com" OR "outlook.com", then a phone number must also be provided',
    },
  },
]


const sharedValidations: IKFormSharedValidationsData = {
  scopes: [
    'Personal Info',
    'Fitness Level',
    'Emergency Contacts',
    'Global',
  ],
  scopeValidations: [
    {
      scope: 'Personal Info',
      validations: [
        {field: 'name', rules: ['NotEmpty']},
        {field: 'email', rules: ['NotEmpty', 'MatchesEmail']},
        {field: 'phoneNumber (nullable)', rules: ['SizePhoneNumber (length 9)', 'ValidFirstDigitPhoneNumber (2 or 9)']},
        {field: 'birthDate', rules: ['Required', 'ValidBirthDate (between 14 and 50 years old']},
      ],
    },
    {
      scope: 'Fitness Level',
      validations: [
        {field: 'rateInfo', rules: ['ValidRateInfo (if rate is Advanced, then should have at least length 10)']},
      ],
    },
    {
      scope: 'Emergency Contacts',
      validations: [
        {field: 'Table', rules: ['MinSize(1)']},
        {field: 'Table > name', rules: ['NotEmpty']},
        {field: 'Table > isFamilyMember', rules: ['Required']},
      ],
    },
    {
      scope: 'Global',
      validations: [
        {field: 'emergencyContacts Table', rules: ['MinTwoEmergencyContactsIfUnderage']},
      ],
    },
  ],
};

export {sharedValidations, serverSideValidations};
