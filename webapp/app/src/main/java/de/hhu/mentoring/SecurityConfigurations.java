package de.hhu.mentoring;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import de.hhu.mentoring.services.accounts.AccountService;

@EnableWebSecurity
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {

	@Autowired
	AccountService account;
	
	@Autowired
	DataSource dataSource;
	
    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
                .antMatchers("/mentor/*" ).hasAuthority("1")
                .antMatchers("/mentor").hasAuthority("1")
                .antMatchers("/organizer/*").hasAuthority("2")
                .antMatchers("/organizer").hasAuthority("2")
                .antMatchers("/student/*").hasAuthority("0")
                .antMatchers("/student").hasAuthority("0")
                .antMatchers("/weiterleiten").hasAnyAuthority("1","0","2")
                .and()
                .logout().logoutSuccessUrl("/")
                .and()
                .formLogin()
                .loginPage("/login")
                .failureForwardUrl("/loginError");

        	
       httpSecurity.csrf().disable();
    }
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    	
    	auth.jdbcAuthentication().dataSource(dataSource)
    	.usersByUsernameQuery("select mail_address,password,enabled from users where mail_address=?")
    	.authoritiesByUsernameQuery("select mail_address,role from users where mail_address=?");
    	
    }
}
