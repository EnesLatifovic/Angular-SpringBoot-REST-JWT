package com.app.api.user;

import com.app.model.customer.Customer;
import com.app.model.customer.CustomerResponse;
import com.app.repo.CustomerRepo;
import com.app.repo.UserRepo;
import io.swagger.annotations.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import com.google.common.base.Strings;
import org.apache.commons.io.IOUtils;
import org.json.*;

import com.app.model.response.*;
import com.app.model.user.*;
import static com.app.model.response.OperationResponse.*;

@RestController
@Api(tags = {"Authentication"})
public class UserController {

	@Autowired
	private UserService userService;
    @Autowired
    private UserRepo userRepo;

	@ApiOperation(value = "Gets current user information", response = UserResponse.class)
	@RequestMapping(value = "/user", method = RequestMethod.GET, produces = {"application/json"})
	public UserResponse getUserInformation(@RequestParam(value = "name", required = false) String userIdParam, HttpServletRequest req) {

		String loggedInUserId = userService.getLoggedInUserId();

		User user;
		boolean provideUserDetails = false;

		if (Strings.isNullOrEmpty(userIdParam)) {
			provideUserDetails = true;
			user = userService.getLoggedInUser();
		}
		else if (loggedInUserId.equals(userIdParam)) {
			provideUserDetails = true;
			user = userService.getLoggedInUser();
		}
		else {
			//Check if the current user is superuser then provide the details of requested user
			provideUserDetails = true;
			user = userService.getUserInfoByUserId(userIdParam);
		}

		UserResponse resp = new UserResponse();
		if (provideUserDetails) {
            resp.setOperationStatus(ResponseStatusEnum.SUCCESS);
		}
		else {
            resp.setOperationStatus(ResponseStatusEnum.NO_ACCESS);
			resp.setOperationMessage("No Access");
		}
		resp.setData(user);
		return resp;
	}
/*
    @ApiOperation(value = "List of users", response = UserResponse.class)
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public UserResponse getUsersByPage(
        @ApiParam(value = ""    )               @RequestParam(value = "page"  ,  defaultValue="1"   ,  required = false) Integer page,
        @ApiParam(value = "between 1 to 1000" ) @RequestParam(value = "size"  ,  defaultValue="20"  ,  required = false) Integer size,
        //@RequestParam(value = "user_id"  , required = false) Integer userId,
        //@RequestParam(value = "company"  , required = false) String company,
        //@RequestParam(value = "country"  , required = false) String country,
        Pageable pageable
    ) {
        UserResponse resp = new UserResponse();
        User user = new User();
        //if (userId != null) { user.setId(userId); }
        //if (company != null)    { qry.setCompany(company); }
        //if (country != null)    { qry.setCountry(country); }

        Page<User> pg = userRepo.findAll(org.springframework.data.domain.Example.of(user), pageable);
        //resp.setPageStats(pg, true);
        resp.setItems(pg.getContent());
        return resp;
    }
*/
	@ApiOperation(value = "Add new user", response = OperationResponse.class)
	@RequestMapping(value = "/user", method = RequestMethod.POST, produces = {"application/json"})
	public OperationResponse addNewUser(@RequestBody User user, HttpServletRequest req) {
		boolean userAddSuccess = userService.addNewUser(user);
		OperationResponse resp = new OperationResponse();
		if (userAddSuccess==true){
      resp.setOperationStatus(ResponseStatusEnum.SUCCESS);
			resp.setOperationMessage("User Added");
		}
		else{
      resp.setOperationStatus(ResponseStatusEnum.ERROR);
			resp.setOperationMessage("Unable to add user");
		}
		return resp;
	}


}
