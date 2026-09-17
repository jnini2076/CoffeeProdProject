import { signal } from "@angular/core";


export const isLoggedIn = signal(!!sessionStorage.getItem('loggedin'));
export const USERNAME = signal(!!sessionStorage.getItem('loggedin'));
