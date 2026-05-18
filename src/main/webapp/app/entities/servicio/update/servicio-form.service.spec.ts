import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../servicio.test-samples';

import { ServicioFormService } from './servicio-form.service';

describe('Servicio Form Service', () => {
  let service: ServicioFormService;

  beforeEach(() => {
    service = TestBed.inject(ServicioFormService);
  });

  describe('Service methods', () => {
    describe('createServicioFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createServicioFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tipoServicio: expect.any(Object),
            zona: expect.any(Object),
            frecuencia: expect.any(Object),
            fecha: expect.any(Object),
            horaInicio: expect.any(Object),
            duracionHoras: expect.any(Object),
            numTrabajadores: expect.any(Object),
            estado: expect.any(Object),
            direccion: expect.any(Object),
            municipio: expect.any(Object),
            notas: expect.any(Object),
            precioTotal: expect.any(Object),
            descuento: expect.any(Object),
            cliente: expect.any(Object),
            tarifa: expect.any(Object),
            trabajadoreses: expect.any(Object),
          }),
        );
      });

      it('passing IServicio should create a new form with FormGroup', () => {
        const formGroup = service.createServicioFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tipoServicio: expect.any(Object),
            zona: expect.any(Object),
            frecuencia: expect.any(Object),
            fecha: expect.any(Object),
            horaInicio: expect.any(Object),
            duracionHoras: expect.any(Object),
            numTrabajadores: expect.any(Object),
            estado: expect.any(Object),
            direccion: expect.any(Object),
            municipio: expect.any(Object),
            notas: expect.any(Object),
            precioTotal: expect.any(Object),
            descuento: expect.any(Object),
            cliente: expect.any(Object),
            tarifa: expect.any(Object),
            trabajadoreses: expect.any(Object),
          }),
        );
      });
    });

    describe('getServicio', () => {
      it('should return NewServicio for default Servicio initial value', () => {
        const formGroup = service.createServicioFormGroup(sampleWithNewData);

        const servicio = service.getServicio(formGroup);

        expect(servicio).toMatchObject(sampleWithNewData);
      });

      it('should return NewServicio for empty Servicio initial value', () => {
        const formGroup = service.createServicioFormGroup();

        const servicio = service.getServicio(formGroup);

        expect(servicio).toMatchObject({});
      });

      it('should return IServicio', () => {
        const formGroup = service.createServicioFormGroup(sampleWithRequiredData);

        const servicio = service.getServicio(formGroup);

        expect(servicio).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IServicio should not enable id FormControl', () => {
        const formGroup = service.createServicioFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewServicio should disable id FormControl', () => {
        const formGroup = service.createServicioFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
