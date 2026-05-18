import dayjs from 'dayjs/esm';

import { IFactura, NewFactura } from './factura.model';

export const sampleWithRequiredData: IFactura = {
  id: 24443,
  numero: 'oof whenever',
  fechaEmision: dayjs('2026-05-18'),
  baseImponible: 26127.31,
  iva: 15295.95,
  total: 29046.28,
  estado: 'EMITIDA',
};

export const sampleWithPartialData: IFactura = {
  id: 27147,
  numero: 'homeschool closed',
  fechaEmision: dayjs('2026-05-18'),
  fechaVencimiento: dayjs('2026-05-18'),
  baseImponible: 18977.88,
  iva: 13801.58,
  total: 26251.34,
  estado: 'EMITIDA',
  notas: 'unibody',
};

export const sampleWithFullData: IFactura = {
  id: 12483,
  numero: 'hourly during till',
  fechaEmision: dayjs('2026-05-18'),
  fechaVencimiento: dayjs('2026-05-17'),
  baseImponible: 6910.93,
  iva: 27897.66,
  total: 10728.16,
  estado: 'EMITIDA',
  notas: 'grimy diver',
};

export const sampleWithNewData: NewFactura = {
  numero: 'boo talkative who',
  fechaEmision: dayjs('2026-05-18'),
  baseImponible: 32706.25,
  iva: 1336.11,
  total: 7867.94,
  estado: 'BORRADOR',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
