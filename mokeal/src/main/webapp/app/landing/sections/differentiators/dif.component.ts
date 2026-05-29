import { CommonModule } from '@angular/common';
import { Component, ViewEncapsulation } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-dif',
  standalone: true,
  templateUrl: './dif.component.html',
  styleUrl: './dif.component.scss',
  encapsulation: ViewEncapsulation.Emulated, 
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,]
})
export default class DifComponent {}