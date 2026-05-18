import { ICliente, NewCliente } from './cliente.model';

export const sampleWithRequiredData: ICliente = {
  id: 2897,
  nombre: 'wherever alongside per',
  telefono: 'for blank above',
  activo: true,
};

export const sampleWithPartialData: ICliente = {
  id: 15834,
  nombre: 'recount',
  telefono: 'coliseum',
  nifCif: 'frightfully',
  direccion: 'down',
  notas: 'usually jumbo nervously',
  activo: false,
};

export const sampleWithFullData: ICliente = {
  id: 17129,
  nombre: 'circumference',
  telefono: 'categorise council',
  email: 'Dorotea4@yahoo.com',
  nifCif: 'bah nephew',
  direccion: 'correctly',
  municipio: 'absent',
  codigoPostal: 'inspection',
  notas: 'wee why',
  activo: true,
};

export const sampleWithNewData: NewCliente = {
  nombre: 'under',
  telefono: 'gratefully',
  activo: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
