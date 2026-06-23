import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'jhi-app',
  template: '<router-outlet />',
  imports: [RouterOutlet],
})
export default class App {}