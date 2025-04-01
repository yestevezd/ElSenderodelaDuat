package com.yestevezd.elsenderodeladuat.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import com.yestevezd.elsenderodeladuat.core.engine.AssetLoader;
import com.yestevezd.elsenderodeladuat.core.engine.InputManager;
import com.yestevezd.elsenderodeladuat.core.game.MainGame;
import com.yestevezd.elsenderodeladuat.core.narrative.NarrativeLine;
import com.yestevezd.elsenderodeladuat.core.narrative.NarrativeManager;
import com.yestevezd.elsenderodeladuat.core.narrative.NarrativeType;
import com.yestevezd.elsenderodeladuat.core.utils.TextUtils;
import com.yestevezd.elsenderodeladuat.core.utils.TextureUtils;
import com.yestevezd.elsenderodeladuat.core.narrative.hieroglyphics.HieroglyphicChar;
import com.yestevezd.elsenderodeladuat.core.narrative.hieroglyphics.HieroglyphicRenderer;

import java.util.List;

public class ContextScreen extends BaseScreen {

    private Texture fondo;
    private BitmapFont font;
    private BitmapFont[] hieroFonts;
    private NarrativeManager narrativeManager;
    private float timeAccumulator = 0f;

    public ContextScreen(MainGame game) {
        super(game);
    }

    @Override
    public void show() {
        AssetLoader.loadContextAssets();
        AssetLoader.finishLoading();

        fondo = AssetLoader.get("others/fondo_contextualizacion.jpg", Texture.class);

        font = new BitmapFont(Gdx.files.internal("fonts/ui_font.fnt"));
        font.getData().setScale(0.9f);

        hieroFonts = new BitmapFont[] {
            new BitmapFont(Gdx.files.internal("fonts/jeroglificos1_font.fnt")),
            new BitmapFont(Gdx.files.internal("fonts/jeroglificos2_font.fnt")),
            new BitmapFont(Gdx.files.internal("fonts/jeroglificos3_font.fnt"))
        };

        for (BitmapFont f : hieroFonts) {
            f.getData().setScale(0.9f);
        }

        narrativeManager = new NarrativeManager();
        narrativeManager.setHieroglyphicFonts(hieroFonts);

        narrativeManager.addLine(new NarrativeLine(
            null,
            null,
            "Primer año del reinado de Ramsés VII (Año 1136 a.C.).",
            NarrativeType.CONTEXTUALIZED,
            "[1]P[2]r[3]i[1]m[1]e[2]r [3]a[2]ñ[1]o [2]d[2]e[1]l [2]r[1]e[1]i[3]n[1]a[1]d[3]o [2]d[1]e [1]R[2]a[1]m[3]s[3]é[2]s [2]V[1]I[2]I [3]([3]A[1]ñ[2]o [2]1[2]1[1]3[3]6 [2]a[1].[3]C[3].[1])[1]."
        ));

        narrativeManager.addLine(new NarrativeLine(
            null,
            null,
            "Egipto se encuentra en tiempos de incertidumbre. El reino está en decadencia, los templos",
            NarrativeType.CONTEXTUALIZED,
            "[2]E[1]g[3]i[3]p[1]t[2]o [2]s[1]e [1]e[1]n[2]c[3]u[3]e[2]n[2]t[1]r[3]a [3]e[2]n [1]t[2]i[3]e[3]m[2]p[3]o[1]s [1]d[1]e [2]i[3]n[2]c[2]e[1]r[1]t[3]i[2]d[1]u[1]m[3]b[2]r[3]e[1]. [1]E[2]l [2]r[1]e[3]i[3]n[1]o [1]e[2]s[2]t[2]á [1]e[3]n [2]d[1]e[2]c[3]a[3]d[1]e[1]n[2]c[2]i[1]a[1], " +
            "[2]l[3]o[1]s [1]t[2]e[3]m[2]p[3]l[1]o[1]s"
        ));

        narrativeManager.addLine(new NarrativeLine(
            null,
            null,
            "son saqueados, la corrupción se extiende y el Valle de los Reyes ha sido profanado. ",
            NarrativeType.CONTEXTUALIZED,
            "[2]s[3]o[3]n [1]s[2]a[1]q[2]u[1]e[3]a[3]d[1]o[2]s[2], [1]l[1]a [2]c[3]o[1]r[2]r[3]u[2]p[1]c[3]i[1]ó[3]n [3]s[2]e [2]e[1]x[3]t[1]i[1]e[3]n[2]d[1]e [2]y [3]e[2]l [1]V[1]a[2]l[3]l[3]e [1]d[2]e [2]l[1]o[1]s [3]R[2]e[1]y[1]e[3]s [1]h[2]a" + 
            "[3]s[3]i[1]d[2]o [3]p[2]r[2]o[1]f[3]a[1]n[1]a[2]d[3]o."
        ));

        narrativeManager.addLine(new NarrativeLine(
            null,
            null,
            "En esta era de sombras, un anciano artesano recibe una carta que cambiará su destino.",
            NarrativeType.CONTEXTUALIZED,
            "[1]E[3]n [2]e[2]s[1]t[3]a [3]e[1]r[2]a [3]d[2]e [1]s[1]o[2]m[3]b[2]r[2]a[1]s[3], [2]u[3]n [1]a[2]n[3]c[3]i[2]a[2]n[1]o [1]a[3]r[2]t[1]e[1]s[2]a[3]n[2]o [3]r[1]e[2]c[3]i[1]b[1]e [2]u[2]n[3]a [1]c[2]a[3]r[1]t[2]a [3]q[3]u[1]e [2]c[1]a[3]m[3]b[2]i[1]a[3]r[2]á [2]s[3]u [1]d[2]e[3]s[1]t[2]i[3]n[3]o[1]."
        ));

        narrativeManager.addLine(new NarrativeLine(
            null,
            null,
            "Es hora de prepararse para el último viaje... aquel que lo llevará más allá de la muerte.",
            NarrativeType.CONTEXTUALIZED,
            "[1]E[2]s [3]h[1]o[2]r[1]a [3]d[2]e [1]p[1]r[2]e[3]p[1]a[2]r[3]a[1]r[1]s[2]e [3]p[1]a[2]r[1]a [1]e[2]l [3]ú[1]l[3]t[3]i[2]m[2]o [3]v[1]i[2]a[3]j[2]e[1].[1].[1]. [3]a[1]q[3]u[2]e[1]l [2]q[3]u[3]e [3]l[1]o [1]l[2]l[2]e[3]v[2]a[2]r[1]á [1]m[2]á[1]s [1]a[3]l[2]l[2]á [2]d[3]e [1]l[2]a [3]m[2]u[1]e[3]r[3]t[2]e[1]."
        ));
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();

        timeAccumulator += delta;

        TextureUtils.drawFullScreen(fondo, batch);
        narrativeManager.update(delta);

        List<NarrativeLine> visibles = narrativeManager.getVisibleLines();
        float y = 500f;

        for (int i = 0; i < visibles.size(); i++) {
            NarrativeLine line = visibles.get(i);
            int chars = narrativeManager.getCharIndexForLine(i);

            if (line.getType() == NarrativeType.CONTEXTUALIZED) {
                List<HieroglyphicChar> fullParsed = line.getParsedHieroglyphics();
                int maxChars = Math.min(chars, fullParsed.size());
                HieroglyphicRenderer.drawCentered(batch, fullParsed.subList(0, maxChars), y);
                y -= 20;

                String translation = line.getTranslation();
                int transChars = Math.max(0, chars - fullParsed.size());
                String translated = translation.substring(0, Math.min(transChars, translation.length()));

                font.setColor(Color.WHITE);
                TextUtils.drawCenteredText(batch, font, translated, y);
                y -= 75;

            } else {
                String translation = line.getTranslation();
                String translated = translation.substring(0, Math.min(chars, translation.length()));
                TextUtils.drawCenteredText(batch, font, translated, y);
                y -= 60;
            }
        }

        if (narrativeManager.isFinished()) {
            TextUtils.drawBlinkingTextCentered(batch, font, "Pulsa ENTER para continuar", 700f, timeAccumulator, 1.0f);
        }

        batch.end();

        if (narrativeManager.isFinished() && InputManager.isSelectPressed()) {
            // TODO: cambiar de pantalla
            // game.setScreen(new HouseScreen(game));
        }
    }

    @Override
    public void dispose() {
        fondo.dispose();
        font.dispose();
        for (BitmapFont f : hieroFonts) f.dispose();
        AssetLoader.unloadContextAssets();
    }
}