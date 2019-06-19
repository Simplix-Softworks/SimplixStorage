package de.leonhard.storage;

class Main {

    public static void main(String[] args) {
        final Toml yaml = new Toml("test", "");

        yaml.setPathPrefix("a.b.c.d.e.f.g.h.i.j.k.l.m.n.o.p.q.r.s.t.u.v.w.x.y.z");
        yaml.set("ficken", true);

        yaml.removeKey("a");
    }
}
