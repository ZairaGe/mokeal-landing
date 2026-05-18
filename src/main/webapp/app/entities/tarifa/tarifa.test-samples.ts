import { ITarifa, NewTarifa } from './tarifa.model';

export const sampleWithRequiredData: ITarifa = {
  id: 32439,
  zona: 'FUERA_COMUNIDAD',
  tipoServicio: 'POST_MUDANZA',
  precioFirstHora: 6449.42,
  precioHoraAdicional: 75.08,
  minimoHoras: 30758,
  activa: false,
};

export const sampleWithPartialData: ITarifa = {
  id: 29318,
  zona: 'COMUNIDAD_MADRID',
  tipoServicio: 'POST_OBRA',
  precioFirstHora: 15905.73,
  precioHoraAdicional: 15175.43,
  minimoHoras: 10725,
  precioPorKm: 7741.72,
  activa: true,
};

export const sampleWithFullData: ITarifa = {
  id: 10500,
  zona: 'MADRID_CAPITAL',
  tipoServicio: 'POST_MUDANZA',
  precioFirstHora: 7534.35,
  precioHoraAdicional: 15380.11,
  minimoHoras: 17916,
  precioPorKm: 32669.25,
  activa: false,
};

export const sampleWithNewData: NewTarifa = {
  zona: 'FUERA_COMUNIDAD',
  tipoServicio: 'EVENTO',
  precioFirstHora: 11081.64,
  precioHoraAdicional: 31307.19,
  minimoHoras: 22290,
  activa: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
