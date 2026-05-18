import { ITrabajador, NewTrabajador } from './trabajador.model';

export const sampleWithRequiredData: ITrabajador = {
  id: 24443,
  nombre: 'wherever devastation',
  telefono: 'once',
  activo: true,
};

export const sampleWithPartialData: ITrabajador = {
  id: 8906,
  nombre: 'hubris whenever noxious',
  telefono: 'per outfit adventuro',
  email: 'Gonzalo23@hotmail.com',
  activo: true,
  notas: 'oof moisten dislocate',
};

export const sampleWithFullData: ITrabajador = {
  id: 6676,
  nombre: 'practical',
  telefono: 'once whereas when',
  email: 'Elena_VillaLozada@hotmail.com',
  activo: true,
  notas: 'thyme draft however',
};

export const sampleWithNewData: NewTrabajador = {
  nombre: 'mentor usually hourly',
  telefono: 'before underneath',
  activo: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
