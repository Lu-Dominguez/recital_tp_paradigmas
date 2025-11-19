conectado :- 
    write('Prolog conectado correctamente!'), 
    nl.
    
cubre_roles(Entrenamientos) :-
    findall(Rol-Req, rol_faltante(Rol, Req), Faltantes),
    

    forall(member(Rol-Req, Faltantes),
           (
             (Req > 0 -> 
                 findall(C, (candidato(C, RHist, _, _), puede_cubrir(C, Rol, RHist, Entrenamientos)), CandidatosQueCubren),
                 length(CandidatosQueCubren, Cubiertos),
                 Cubiertos >= Req
             ; 
                 true
             )
           )
    ).


puede_cubrir(Candidato, Rol, RolesHistoricos, Entrenamientos) :-
    member(Rol, RolesHistoricos); 
    member(Candidato-Rol, Entrenamientos).


entrenamientos_minimos(MinimoEntrenamientos, SolucionMinima) :-
    % Genera todas las posibles combinaciones de entrenamientos
    setof(NumEntrenamientos-Entrenamientos, 
          (   % Genera una lista de posibles entrenamientos que cumplen la restricción
              posibles_entrenamientos(Posibles), 
              subconjunto_entrenamientos(Posibles, Entrenamientos),
              cubre_roles(Entrenamientos),
              length(Entrenamientos, NumEntrenamientos)
          ), 
          ListaOrdenada),
    ListaOrdenada = [MinimoEntrenamientos-SolucionMinima | _].


posibles_entrenamientos(Posibles) :-
    findall(Candidato-Rol, 
            (   candidato(Candidato, RolesHist, _, _), 
                rol_faltante(Rol, Req), % Solo roles que realmente faltan
                Req > 0,
                \+ member(Rol, RolesHist) % El candidato NO tiene el rol históricamente (necesita entrenamiento)
            ), 
            Posibles).


subconjunto_entrenamientos([], []).
subconjunto_entrenamientos([H|T], [H|Sub]):- 
    subconjunto_entrenamientos(T, Sub).
subconjunto_entrenamientos([_|T], Sub):- 
    subconjunto_entrenamientos(T, Sub).