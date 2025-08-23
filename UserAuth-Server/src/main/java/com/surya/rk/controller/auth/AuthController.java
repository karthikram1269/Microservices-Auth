package com.surya.rk.controller.auth;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.surya.rk.dto.AuthenticationRequest;
import com.surya.rk.dto.AuthenticationResponse;
import com.surya.rk.dto.SignupRequest;
import com.surya.rk.dto.UserDto;
import com.surya.rk.entities.User;
import com.surya.rk.repositories.UserRepository;
import com.surya.rk.services.auth.AuthService;
import com.surya.rk.services.jwt.UserService;
import com.surya.rk.utils.JwtUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

	private final AuthService authService;
	
	private final UserRepository userRepository;
	
	private final JwtUtil jwtUtil;
	private final UserService userService;
	
	private final AuthenticationManager authenticationManager;
		
	
	private final Logger logger=LoggerFactory.getLogger(AuthController.class);
	
	
	@PostMapping("/signup")
	public ResponseEntity<?> signupUser(@Valid @RequestBody SignupRequest signupRequest)
	{
	
		if(authService.hasUserWithEmail(signupRequest.getEmail()))
		{
			
			logger.warn("user already exist with this email {}",signupRequest.getEmail());
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("user already exist with this email");
		}
		UserDto createdUserDto=authService.signupUser(signupRequest);
		if(createdUserDto==null)
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not created");
		}
		logger.info("User created Succesfuly ");
		return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto);
	}
	

	

	
	@PostMapping("/login")
	public AuthenticationResponse login(@RequestBody AuthenticationRequest authenticationRequest)
	{
		try
		{
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
					(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
		}catch (BadCredentialsException e) {
			logger.debug("Incorrect password or email!");
			throw new BadCredentialsException("Incorrect password or email!");
		}
		    final UserDetails userDetails=userService.userDetailsService().loadUserByUsername(authenticationRequest.getEmail());
			Optional<User>optionalUser=	userRepository.findFirstByEmail(authenticationRequest.getEmail());
			final String jwtToken=jwtUtil.generateToken(userDetails);
			AuthenticationResponse authenticationResponse=new AuthenticationResponse();
			if(optionalUser.isPresent())
			{
				authenticationResponse.setJwt(jwtToken);
				authenticationResponse.setUserId(optionalUser.get().getId());
				authenticationResponse.setUserRole(optionalUser.get().getUserRole());
			}
			return  authenticationResponse;
	}


	
}
