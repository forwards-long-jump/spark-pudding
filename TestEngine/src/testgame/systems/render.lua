function getRequiredComponents()
  return {colored = {"position", "size", "color"},
          texts = {"color", "position", "text"},
          textured = {"position", "size", "texture"},
          triangles = {"triangle", "size", "position", "color"}}
end

function render()
  game.camera:applyTransforms(g:getContext())
  renderCount = 0

  -- Triangles
  for i, entity in ipairs(triangles) do
    pos = entity.position
    size = entity.size
    points = entity.triangle
    p1 = {pos.x + points.dx1, pos.y + points.dy1}
    p2 = {pos.x + points.dx2, pos.y + points.dy2}
    p3 = {pos.x + points.dx3, pos.y + points.dy3}
    
     if game.camera:isInView(pos.x, pos.y, size.width, size.height) then
      renderCount = renderCount + 1
      g:setColor(game.color:fromRGB(color.r, color.g, color.b))
      g:fillPolygon({p1,p2,p3})
    end
  end

  -- Basic rectangles
  for i, entity in ipairs(colored) do
    if entity.triangle == nil then
      pos = entity.position
      size = entity.size
      color = entity.color
  
      if game.camera:isInView(pos.x, pos.y, size.width, size.height) then
        renderCount = renderCount + 1
        g:setColor(game.color:fromRGB(color.r, color.g, color.b))
        g:fillRect(pos, size)
      end
    end
  end

  -- Text
  for i, entity in ipairs(texts) do
    pos = entity.position
    size = entity.size
    color = entity.color

    renderCount = renderCount + 1
    g:setColor(game.color:fromRGB(color.r, color.g, color.b))
    g:drawString(entity.text.value, pos)
  end
  
  -- Rectangles with texture
  for i, entity in ipairs(textured) do
    pos = entity.position
    size = entity.size
    if game.camera:isInView(pos.x, pos.y, size.width, size.height) then
      renderCount = renderCount + 1
      g:drawImage(entity.texture.filename, pos, size)
    end
  end

  game.camera:resetTransforms(g:getContext())

  -- Optional : FPS counter and drawn entities counter

  g:setColor(game.color:fromRGB(0, 0, 0))
  g:drawString("FPS: " .. game.core:getFPS(), 20, 20)
  g:drawString("EC : " .. renderCount, 20, 40)

end
