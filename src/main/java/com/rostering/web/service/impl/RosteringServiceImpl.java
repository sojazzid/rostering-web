package com.rostering.web.service.impl;

import com.rostering.web.service.RosteringService;

public class RosteringServiceImpl implements RosteringService {

    /*
    @Override
    public List<Maquinista> getMaquinistasList() {

        int nroMaquinistas = 5;

        List<Maquinista> maquinistas = new ArrayList<Maquinista>(nroMaquinistas);

        for (int i = 1; i <= nroMaquinistas; i++) {

            Maquinista maquinista = new Maquinista();

            maquinista.setId(i);
            maquinista.setName("Nombre Maquinista " + i);

            maquinistas.add(maquinista);
        }

        return maquinistas;
    }

    @Override
    public List<Turno> getTurnosList() {
        int nroTurnos = 5;

        List<Turno> turnos = new ArrayList<Turno>(nroTurnos);

        for (int i = 1; i <= nroTurnos; i++) {

            Turno turno = new Turno();
            turno.setId(i);
            turno.setName("Nombre Maquinista " + i);

            turnos.add(turno);
        }

        return turnos;
    }

    @Override
    public List<Asignacion> asignarTurnos(List<Maquinista> maquinistas, List<Turno> turnos) {

        int nroAsignaciones = 5;

        List<Asignacion> asignaciones = new ArrayList<Asignacion>(nroAsignaciones);

        for (int i = 1; i <= nroAsignaciones; i++) {

            double random = Math.random();

            Maquinista maquinista = new Maquinista();

            maquinista.setId(i);
            maquinista.setName("Nombre Maquinista " + random);

            Turno turno = new Turno();
            turno.setId(i);
            turno.setName("Nombre Maquinista " + random);

            Asignacion asignacion = new Asignacion();
            asignacion.setMaquinista(maquinista);
            asignacion.setTurno(turno);

            asignaciones.add(asignacion);
        }

        return asignaciones;
    }

    @Override
    public boolean save(String name, List<Asignacion> asignaciones) {
        return true;
    }
    */
}

