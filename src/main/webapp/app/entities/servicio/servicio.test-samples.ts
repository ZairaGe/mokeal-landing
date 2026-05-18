import dayjs from 'dayjs/esm';

import { IServicio, NewServicio } from './servicio.model';

export const sampleWithRequiredData: IServicio = {
  id: 27960,
  tipoServicio: 'POST_MUDANZA',
  zona: 'COMUNIDAD_MADRID',
  frecuencia: 'PUNTUAL',
  fecha: dayjs('2026-05-17'),
  horaInicio: 'genui',
  duracionHoras: 28802.11,
  numTrabajadores: 20250,
  estado: 'COMPLETADO',
};

export const sampleWithPartialData: IServicio = {
  id: 6563,
  tipoServicio: 'OFICINA',
  zona: 'MADRID_CAPITAL',
  frecuencia: 'SEMANAL',
  fecha: dayjs('2026-05-18'),
  horaInicio: 'edito',
  duracionHoras: 25734.83,
  numTrabajadores: 26832,
  estado: 'COMPLETADO',
  notas: 'fearless',
  precioTotal: 15734.96,
  descuento: 18994.72,
};

export const sampleWithFullData: IServicio = {
  id: 15992,
  tipoServicio: 'EVENTO',
  zona: 'MADRID_CAPITAL',
  frecuencia: 'MENSUAL',
  fecha: dayjs('2026-05-18'),
  horaInicio: 'withi',
  duracionHoras: 23230.73,
  numTrabajadores: 27647,
  estado: 'PENDIENTE',
  direccion: 'beneath swerve tackle',
  municipio: 'lid',
  notas: 'than cutover',
  precioTotal: 23073.49,
  descuento: 20077.66,
};

export const sampleWithNewData: NewServicio = {
  tipoServicio: 'OFICINA',
  zona: 'FUERA_COMUNIDAD',
  frecuencia: 'SEMANAL',
  fecha: dayjs('2026-05-17'),
  horaInicio: 'pranc',
  duracionHoras: 19679.1,
  numTrabajadores: 2151,
  estado: 'CONFIRMADO',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
