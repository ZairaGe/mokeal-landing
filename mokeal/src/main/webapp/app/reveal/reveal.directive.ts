import { Directive, ElementRef, Input, OnInit, inject } from '@angular/core';

@Directive({
  selector: '[jhiReveal]',
  standalone: true,
})
export default class RevealDirective implements OnInit {
  @Input('jhiReveal') direction: 'up' | 'left' | 'right' | 'none' = 'up';
  @Input() revealDelay = 0;

  private el = inject(ElementRef);

  ngOnInit(): void {
    const element = this.el.nativeElement as HTMLElement;

    const transforms: Record<string, string> = {
      up: 'translateY(30px)',
      left: 'translateX(-30px)',
      right: 'translateX(30px)',
      none: 'translateY(0)',
    };

    element.style.opacity = '0';
    element.style.transform = transforms[this.direction] ?? 'translateY(30px)';
    element.style.transition = `opacity 0.6s ease ${this.revealDelay}ms, transform 0.6s ease ${this.revealDelay}ms`;

    const observer = new IntersectionObserver(
      entries => {
        entries.forEach(entry => {
          if (entry.isIntersecting) {
            element.style.opacity = '1';
            element.style.transform = 'translate(0)';
            observer.unobserve(element);
          }
        });
      },
      { threshold: 0.15 }
    );

    observer.observe(element);
  }
}