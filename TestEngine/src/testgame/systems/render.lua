function getRequiredComponents()
  return {colored = {"position", "size", "color"},
          texts = {"color", "position", "text"},
          textured = {"position", "size", "texture"}}
end

function render(g)
  game.camera:applyTransforms(g)
  renderCount = 0

  -- Basic rectangles
  for i, entity in ipairs(colored) do
    pos = entity.position
    size = entity.size
    color = entity.color
    if game.camera:isInView(pos.x, pos.y, size.width, size.height) then
      renderCount = renderCount + 1
      g:setColor(game.color:fromRGB(color.r, color.g, color.b))
      g:fillRect(pos.x, pos.y, size.width, size.height)
    end
  end

  -- Text
  for i, entity in ipairs(texts) do
    pos = entity.position
    size = entity.size
    color = entity.color

    renderCount = renderCount + 1
    g:setColor(game.color:fromRGB(color.r, color.g, color.b))
    g:drawString(entity.text.value, pos.x, pos.y)
  end
  
  -- Rectangles with texture
  for i, entity in ipairs(textured) do
    pos = entity.position
    size = entity.size
    img = game.resources:getTexture(entity.texture.filename)
    if game.camera:isInView(pos.x, pos.y, size.width, size.height) then
      renderCount = renderCount + 1
      g:drawImage(img, pos.x, pos.y, size.width, size.height)
    end
  end

  game.camera:resetTransforms(g)

  -- Optional : FPS counter and drawn entities counter

  g:setColor(game.color:fromRGB(0, 0, 0))
  g:drawString("FPS: " .. game.core:getFPS(), 20, 20)
  g:drawString("EC : " .. renderCount, 20, 40)

end
