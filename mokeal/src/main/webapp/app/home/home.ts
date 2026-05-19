import { Component, inject, OnInit, signal, AfterViewInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { LoginService } from 'app/login/login.service';
import { DecimalPipe, NgClass } from '@angular/common';
import { TarifaService } from 'app/entities/tarifa/service/tarifa.service';
import { ITarifa } from 'app/entities/tarifa/tarifa.model';
import { FacturaService } from 'app/entities/factura/service/factura.service';
import { IFactura } from 'app/entities/factura/factura.model';

import * as L from 'leaflet';
import { IServicio } from 'app/entities/servicio/servicio.model';
import { ServicioService } from 'app/entities/servicio/service/servicio.service';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.html',
  styleUrl: './home.scss',
  imports: [RouterModule, NgClass, DecimalPipe],
})
export default class HomeComponent implements OnInit, AfterViewInit {
  account = signal<Account | null>(null);
  activeTab = 'hoy';
  tarifas = signal<ITarifa[]>([]);
  facturas = signal<IFactura[]>([]);
  servicios = signal<IServicio[]>([]);
  private facturaService = inject(FacturaService);
  private tarifaService = inject(TarifaService);

  private accountService = inject(AccountService);
  private loginService = inject(LoginService);
  private router = inject(Router);
  private map!: L.Map;

  private initMap(): void {
    this.map = L.map('map').setView([40.4167, -3.7037], 13);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '© OpenStreetMap contributors',
    }).addTo(this.map);

    // --- MUEVE EL CÁLCULO AQUÍ ---
    const puntoA = L.latLng(40.4167, -3.7037);
    const puntoB = L.latLng(40.4233, -3.6821);

    const distanciaEnMetros = puntoA.distanceTo(puntoB);
    const iconDefault = L.icon({
      iconRetinaUrl: 'assets/leaflet/marker-icon-2x.png',
      iconUrl: 'assets/leaflet/marker-icon.png',
      shadowUrl: 'assets/leaflet/marker-shadow.png',
      iconSize: [25, 41],
      iconAnchor: [12, 41],
      popupAnchor: [1, -34],
      shadowSize: [41, 41],
    });
    L.Marker.prototype.options.icon = iconDefault;

    // Sustituye los L.marker por esto:
    L.circleMarker([40.4167, -3.7037], {
      radius: 10, // Tamaño del punto
      fillColor: '#ff7800', // Color de relleno
      color: '#000', // Color del borde
      weight: 1,
      opacity: 1,
      fillOpacity: 0.8,
    })
      .addTo(this.map)
      .bindPopup('Punto de Servicio');

    this.map = L.map('map').setView([40.4167, -3.7037], 13);

    // Nota: Asegúrate de usar comillas invertidas ` `
    console.log(`La distancia es de ${(distanciaEnMetros / 1000).toFixed(2)} km`);

    // Añadir marcadores para visualizarlo
    L.marker(puntoA).addTo(this.map).bindPopup('Punto A');
    L.marker(puntoB).addTo(this.map).bindPopup('Punto B');
  }

  // Suponiendo que recibes una lista de servicios desde tu servicio de Angular
  dibujarServiciosEnMapa(servicios: IServicio[]): void {
    servicios.forEach(s => {
      if (s.latitud && s.longitud) {
        L.marker([s.latitud, s.longitud]).addTo(this.map).bindPopup(`<b>${s.tipoServicio}</b><br>${s.direccion}`);
      }
    });
  }

  ngAfterViewInit(): void {
    this.initMap();
  }

  constructor(private servicioService: ServicioService) {}

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => {
      this.account.set(account);
      if (account) {
        // 1. Traemos tarifas
        this.tarifaService.query().subscribe(res => this.tarifas.set(res.body ?? []));

        // 2. Traemos facturas
        this.facturaService.query().subscribe(res => this.facturas.set(res.body ?? []));

        // 3. Traemos SERVICIOS para el mapa
        this.servicioService.query().subscribe(res => {
          const listaServicios = res.body ?? [];
          this.servicios.set(listaServicios); // Guardamos en la señal

          // Dibujamos los puntos en el mapa
          this.actualizarPuntosMapa(listaServicios);
        });
      }
    });
  }

  private actualizarPuntosMapa(servicios: IServicio[]): void {
    if (!this.map) return;

    servicios.forEach(s => {
      // Verificamos que el servicio tenga coordenadas (latitud y longitud)
      if (s.latitud && s.longitud) {
        L.circleMarker([s.latitud, s.longitud], {
          radius: 8,
          fillColor: '#ff7800', // El naranja que ya te funciona
          color: '#000',
          weight: 1,
          fillOpacity: 0.8,
        })
          .addTo(this.map!)
          .bindPopup(`<b>${s.tipoServicio}</b><br>${s.direccion}`); // Usamos tipoServicio para evitar error de 'nombre'
      }
    });
  }

  private addMarkers(): void {
    this.servicios().forEach(s => {
      if (s.latitud && s.longitud) {
        L.circleMarker([s.latitud, s.longitud], {
          radius: 8,
          fillColor: '#ff7800',
          color: '#000',
          weight: 1,
          fillOpacity: 0.8,
        })
          .addTo(this.map!)

          .bindPopup(`<b>${s.tipoServicio}</b><br>${s.direccion}`);
      }
    });
  }

  calcularCercania(servicioA: IServicio, servicioB: IServicio): number {
    if (servicioA.latitud && servicioA.longitud && servicioB.latitud && servicioB.longitud) {
      const punto1 = L.latLng(servicioA.latitud, servicioA.longitud);
      const punto2 = L.latLng(servicioB.latitud, servicioB.longitud);

      // Retorna la distancia en kilómetros
      return punto1.distanceTo(punto2) / 1000;
    }
    return 0;
  }

  setTab(tab: string): void {
    this.activeTab = tab;
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  getTipoLabel(tipo: string | null | undefined): string {
    const labels: Record<string, string> = {
      HOGAR: 'Hogar / Piso',
      OFICINA: 'Oficina / Local',
      CHALET: 'Chalet',
      EVENTO: 'Evento',
      POST_OBRA: 'Post-obra',
      POST_MUDANZA: 'Post-mudanza',
    };
    return tipo ? (labels[tipo] ?? tipo) : '—';
  }

  getZonaLabel(zona: string): string {
    const labels: Record<string, string> = {
      MADRID_CAPITAL: 'Madrid Capital',
      COMUNIDAD_MADRID: 'Comunidad de Madrid',
      FUERA_COMUNIDAD: 'Fuera de la Comunidad',
    };
    return labels[zona] ?? zona;
  }

  getZonaClass(zona: string): string {
    return { MADRID_CAPITAL: 'zona-madrid', COMUNIDAD_MADRID: 'zona-fuera', FUERA_COMUNIDAD: 'zona-evento' }[zona] ?? '';
  }

  getZonaBadgeClass(zona: string): string {
    return { MADRID_CAPITAL: 'badge-madrid', COMUNIDAD_MADRID: 'badge-fuera', FUERA_COMUNIDAD: 'badge-evento' }[zona] ?? '';
  }

  getTarifasPorZona(zona: string): ITarifa[] {
    return this.tarifas().filter(t => t.zona === zona);
  }
  getTotalFacturado(): number {
    return this.facturas().reduce((acc, f) => acc + (Number(f.total) || 0), 0);
  }

  getTotalCobrado(): number {
    return this.facturas()
      .filter(f => f.estado === 'PAGADA')
      .reduce((acc, f) => acc + (Number(f.total) || 0), 0);
  }

  getTotalPendiente(): number {
    return this.facturas()
      .filter(f => f.estado === 'EMITIDA' || f.estado === 'BORRADOR')
      .reduce((acc, f) => acc + (Number(f.total) || 0), 0);
  }

  getFacturaEstadoLabel(estado: string | null | undefined): string {
    const labels: Record<string, string> = {
      BORRADOR: 'Borrador',
      EMITIDA: 'Emitida',
      PAGADA: 'Pagada',
      CANCELADA: 'Cancelada',
    };
    return estado ? (labels[estado] ?? estado) : '—';
  }

  getFacturaBadgeClass(estado: string | null | undefined): string {
    const classes: Record<string, string> = {
      BORRADOR: 'b-pendiente',
      EMITIDA: 'b-fuera',
      PAGADA: 'b-confirmado',
      CANCELADA: 'b-pendiente',
    };
    return estado ? (classes[estado] ?? '') : '';
  }
}
