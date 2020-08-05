import { Component, OnInit } from '@angular/core';
import { LoginService } from '../../services/api/login.service';
import { Router } from '@angular/router';
import * as bcrypt from 'bcryptjs';

@Component({
	selector   : 's-login-pg',
	templateUrl: './login.component.html',
    styleUrls  : [ './login.scss'],
})

export class LoginComponent implements OnInit {
    model: any = {};
    errMsg:string = '';
    constructor(
        private router: Router,
        private loginService: LoginService) { }

    ngOnInit() {
        // reset login status
        this.loginService.logout(false);
    }

    login() {
        const salt = bcrypt.genSaltSync(10);
        this.loginService.getToken(this.model.username, bcrypt.hashSync(this.model.password, salt))
            .subscribe(resp => {
                    if (resp.user === undefined || resp.user.token === undefined || resp.user.token === "INVALID" ){
                        this.errMsg = 'Username or password is incorrect';
                        return;
                    }
                    this.router.navigate([resp.landingPage]);
                },
                errResponse => {
                  switch(errResponse.status){
                    case 401:
                      this.errMsg = 'Username or password is incorrect';
                      break;
                    case 404:
                      this.errMsg = 'Service not found';
                    case 408:
                      this.errMsg = 'Request Timedout';
                    case 500:
                      this.errMsg = 'Internal Server Error';
                    default:
                      this.errMsg = 'Server Error';
                  }
                }
            );
    }

    onSignUp(){
      this.router.navigate(['signup']);
    }


}
