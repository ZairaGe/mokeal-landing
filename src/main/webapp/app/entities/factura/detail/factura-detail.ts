import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateModule } from '@ngx-translate/core';

import { Alert } from 'app/shared/alert/alert';
import { AlertError } from 'app/shared/alert/alert-error';
import { FormatMediumDatePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IFactura } from '../factura.model';

@Component({
  selector: 'jhi-factura-detail',
  templateUrl: './factura-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, TranslateModule, RouterLink, FormatMediumDatePipe],
})
export class FacturaDetail {
  readonly factura = input<IFactura | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
