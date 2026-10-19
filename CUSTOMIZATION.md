# Personalização visual (branch `custom/apple-style`)

Esta branch muda **só o layout e o visual** das telas do OwnTV. Lógica, reprodução, banco de dados,
identificador do app, assinatura e atualizador continuam como no projeto original.

## Onde ficam as mudanças

| Arquivo | O que muda | Por quê |
|---|---|---|
| `app/src/main/java/tv/own/owntv/ui/theme/CustomDesign.kt` | **Novo.** Paleta grafite, cores do foco, fundo | Concentra todos os valores do visual novo |
| `ui/theme/Theme.kt` | Usa `CustomPalettes` no lugar de `StagePalette` | Aplica a paleta nova ao app todo |
| `ui/components/TrailerPlayerScreen.kt` | Idem | Mesmo motivo |
| `ui/theme/StageTokens.kt` | `stageAccent` usa `CustomFocus`; botões e pílulas viram cápsulas | Foco em cápsula branca |
| `ui/stage/StageDraw.kt` | Fundo das telas usa `CustomBackground` | Fundo grafite neutro |
| `ui/stage/StageComponents.kt` | Novo desenho de foco; `StageStill` com legenda sobre a imagem (`StillCaption`); `StagePoster` com `rank` e `titleOnArt` | Cards e foco das referências |
| `features/shell/components/StageRail.kt` | Menu lateral: pílula recolhida e painel com perfil e hora | Prints 6 e 7 |
| `features/home/HomeScreen.kt` | Destaque com botões redondos, pontos de página, fileiras | Prints 4, 5 e 6 |

Todo trecho alterado em arquivo original tem um comentário começando com `Personalização`.

## Efeitos colaterais conhecidos

- A **cor de destaque** de Ajustes não pinta mais menus, botões e cards (o foco agora é branco). No
  player ela continua valendo.
- No destaque da Home, **Trailer** e **Todas as versões** viraram botões só com ícone.

## Receber atualizações do projeto original

```bash
git remote add upstream https://github.com/ahXN00/OwnTV.git   # uma vez só
git fetch upstream
git checkout custom/apple-style
git merge upstream/main
```

Se houver conflito, ele quase sempre cai num dos arquivos da tabela acima. Para resolver:

1. Abra o arquivo e procure os marcadores `<<<<<<<`.
2. Fique com o código novo do upstream e reaplique por cima só os trechos marcados com
   `Personalização` (o `git diff upstream/main -- <arquivo>` mostra quais são).
3. `git add <arquivo>` e `git commit`.

Para trazer só uma correção específica: `git cherry-pick <commit-do-upstream>`.

A biblioteca **OwnTV_Core** (banco, parsers, motores de vídeo) vem pronta do Maven do projeto, na
versão fixada em `gradle/libs.versions.toml` (`owntvCore`). Ela não é alterada aqui; um merge do
upstream que suba essa versão traz as correções do Core junto.

## Compilar

O APK de teste é gerado pelo GitHub Actions do fork: aba **Actions** → **Android CI** → **Run
workflow** → branch `custom/apple-style`. O APK fica em **Artifacts** no fim da execução. Sem os
segredos de assinatura do projeto original, ele sai assinado com chave de debug: não instala por cima
do OwnTV oficial. Faça o backup `.own` no app oficial, desinstale-o, instale este e restaure o backup.
