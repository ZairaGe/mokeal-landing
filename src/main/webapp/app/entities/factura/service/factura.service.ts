import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { isPresent } from 'app/core/util/operators';
import { IFactura, NewFactura } from '../factura.model';

export type PartialUpdateFactura = Partial<IFactura> & Pick<IFactura, 'id'>;

type RestOf<T extends IFactura | NewFactura> = Omit<T, 'fechaEmision' | 'fechaVencimiento'> & {
  fechaEmision?: string | null;
  fechaVencimiento?: string | null;
};

export type RestFactura = RestOf<IFactura>;

export type NewRestFactura = RestOf<NewFactura>;

export type PartialUpdateRestFactura = RestOf<PartialUpdateFactura>;

@Injectable()
export class FacturasService {
  readonly facturasParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly facturasResource = httpResource<RestFactura[]>(() => {
    const params = this.facturasParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of factura that have been fetched. It is updated when the facturasResource emits a new value.
   * In case of error while fetching the facturas, the signal is set to an empty array.
   */
  readonly facturas = computed(() =>
    (this.facturasResource.hasValue() ? this.facturasResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly applicationConfigService = inject(ApplicationConfigService);
  protected readonly resourceUrl = this.applicationConfigService.getEndpointFor('api/facturas');

  protected convertValueFromServer(restFactura: RestFactura): IFactura {
    return {
      ...restFactura,
      fechaEmision: restFactura.fechaEmision ? dayjs(restFactura.fechaEmision) : undefined,
      fechaVencimiento: restFactura.fechaVencimiento ? dayjs(restFactura.fechaVencimiento) : undefined,
    };
  }
}

@Injectable({ providedIn: 'root' })
export class FacturaService extends FacturasService {
  protected readonly http = inject(HttpClient);

  create(factura: NewFactura): Observable<IFactura> {
    const copy = this.convertValueFromClient(factura);
    return this.http.post<RestFactura>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(factura: IFactura): Observable<IFactura> {
    const copy = this.convertValueFromClient(factura);
    return this.http
      .put<RestFactura>(`${this.resourceUrl}/${encodeURIComponent(this.getFacturaIdentifier(factura))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(factura: PartialUpdateFactura): Observable<IFactura> {
    const copy = this.convertValueFromClient(factura);
    return this.http
      .patch<RestFactura>(`${this.resourceUrl}/${encodeURIComponent(this.getFacturaIdentifier(factura))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IFactura> {
    return this.http
      .get<RestFactura>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IFactura[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFactura[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getFacturaIdentifier(factura: Pick<IFactura, 'id'>): number {
    return factura.id;
  }

  compareFactura(o1: Pick<IFactura, 'id'> | null, o2: Pick<IFactura, 'id'> | null): boolean {
    return o1 && o2 ? this.getFacturaIdentifier(o1) === this.getFacturaIdentifier(o2) : o1 === o2;
  }

  addFacturaToCollectionIfMissing<Type extends Pick<IFactura, 'id'>>(
    facturaCollection: Type[],
    ...facturasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const facturas: Type[] = facturasToCheck.filter(isPresent);
    if (facturas.length > 0) {
      const facturaCollectionIdentifiers = facturaCollection.map(facturaItem => this.getFacturaIdentifier(facturaItem));
      const facturasToAdd = facturas.filter(facturaItem => {
        const facturaIdentifier = this.getFacturaIdentifier(facturaItem);
        if (facturaCollectionIdentifiers.includes(facturaIdentifier)) {
          return false;
        }
        facturaCollectionIdentifiers.push(facturaIdentifier);
        return true;
      });
      return [...facturasToAdd, ...facturaCollection];
    }
    return facturaCollection;
  }

  protected convertValueFromClient<T extends IFactura | NewFactura | PartialUpdateFactura>(factura: T): RestOf<T> {
    return {
      ...factura,
      fechaEmision: factura.fechaEmision?.format(DATE_FORMAT) ?? null,
      fechaVencimiento: factura.fechaVencimiento?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertResponseFromServer(res: RestFactura): IFactura {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestFactura[]): IFactura[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
