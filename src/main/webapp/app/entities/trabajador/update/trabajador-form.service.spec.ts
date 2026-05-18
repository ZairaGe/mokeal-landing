import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../trabajador.test-samples';

import { TrabajadorFormService } from './trabajador-form.service';

describe('Trabajador Form Service', () => {
  let service: TrabajadorFormService;

  beforeEach(() => {
    service = TestBed.inject(TrabajadorFormService);
  });

  describe('Service methods', () => {
    describe('createTrabajadorFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTrabajadorFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            telefono: expect.any(Object),
            email: expect.any(Object),
            activo: expect.any(Object),
            notas: expect.any(Object),
            servicioses: expect.any(Object),
          }),
        );
      });

      it('passing ITrabajador should create a new form with FormGroup', () => {
        const formGroup = service.createTrabajadorFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            telefono: expect.any(Object),
            email: expect.any(Object),
            activo: expect.any(Object),
            notas: expect.any(Object),
            servicioses: expect.any(Object),
          }),
        );
      });
    });

    describe('getTrabajador', () => {
      it('should return NewTrabajador for default Trabajador initial value', () => {
        const formGroup = service.createTrabajadorFormGroup(sampleWithNewData);

        const trabajador = service.getTrabajador(formGroup);

        expect(trabajador).toMatchObject(sampleWithNewData);
      });

      it('should return NewTrabajador for empty Trabajador initial value', () => {
        const formGroup = service.createTrabajadorFormGroup();

        const trabajador = service.getTrabajador(formGroup);

        expect(trabajador).toMatchObject({});
      });

      it('should return ITrabajador', () => {
        const formGroup = service.createTrabajadorFormGroup(sampleWithRequiredData);

        const trabajador = service.getTrabajador(formGroup);

        expect(trabajador).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITrabajador should not enable id FormControl', () => {
        const formGroup = service.createTrabajadorFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTrabajador should disable id FormControl', () => {
        const formGroup = service.createTrabajadorFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
