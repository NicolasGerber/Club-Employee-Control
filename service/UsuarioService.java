package club_employee_control.service;

import club_employee_control.dto.RegistroRequest;
import club_employee_control.entity.Role;
import club_employee_control.entity.Usuario;
import club_employee_control.repository.UsuarioRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
    }

    public void registrar(RegistroRequest request) {
        if (request.role() == Role.ADMIN) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            boolean isAdmin = auth != null &&
                    auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

            if (!isAdmin) {
                throw new AccessDeniedException("Apenas ADMIN pode criar outro ADMIN");
            }
        }

        Usuario usuario = new Usuario(
                request.email(),
                passwordEncoder.encode(request.senha()),
                request.role()
        );

        usuarioRepository.save(usuario);
    }
}