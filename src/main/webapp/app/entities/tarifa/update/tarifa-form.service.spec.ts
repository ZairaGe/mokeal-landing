import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../tarifa.test-samples';

import { TarifaFormService } from './tarifa-form.service';

describe('Tarifa Form Service', () => {
  let service: TarifaFormService;

  beforeEach(() => {
    service = TestBed.inject(TarifaFormService);
  });

  describe('Service methods', () => {
    describe('createTarifaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTarifaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            zona: expect.any(Object),
            tipoServicio: expect.any(Object),
            precioFirstHora: expect.any(Object),
            precioHoraAdicional: expect.any(Object),
            minimoHoras: expect.any(Object),
            precioPorKm: expect.any(Object),
            activa: expect.any(Object),
          }),
        );
      });

      it('passing ITarifa should create a new form with FormGroup', () => {
        const formGroup = service.createTarifaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            zona: expect.any(Object),
            tipoServicio: expect.any(Object),
            precioFirstHora: expect.any(Object),
            precioHoraAdicional: expect.any(Object),
            minimoHoras: expect.any(Object),
            precioPorKm: expect.any(Object),
            activa: expect.any(Object),
          }),
        );
      });
    });

    describe('getTarifa', () => {
      it('should return NewTarifa for default Tarifa initial value', () => {
        const formGroup = service.createTarifaFormGroup(sampleWithNewData);

        const tarifa = service.getTarifa(formGroup);

        expect(tarifa).toMatchObject(sampleWithNewData);
      });

      it('should return NewTarifa for empty Tarifa initial value', () => {
        const formGroup = service.createTarifaFormGroup();

        const tarifa = service.getTarifa(formGroup);

        expect(tarifa).toMatchObject({});
      });

      it('should return ITarifa', () => {
        const formGroup = service.createTarifaFormGroup(sampleWithRequiredData);

        const tarifa = service.getTarifa(formGroup);

        expect(tarifa).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITarifa should not enable id FormControl', () => {
        const formGroup = service.createTarifaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTarifa should disable id FormControl', () => {
        const formGroup = service.createTarifaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
