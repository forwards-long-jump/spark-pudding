function getRequiredComponents()
  return {players = {"position", "size", "color"}, texts = {"color", "position", "text"}}
end

function render(g)
  for i, entity in ipairs(players) do
    pos = entity.position
    size = entity.size
    color = entity.color

    g:setColor(game.color:fromRGB(color.r, color.g, color.b))
    g:fillRect(pos.x, pos.y, size.width, size.height)
  end

  for i, entity in ipairs(texts) do
    pos = entity.position
    size = entity.size
    color = entity.color

    g:setColor(game.color:fromRGB(color.r, color.g, color.b))
    g:drawString(entity.text.value, pos.x, pos.y)
  end
end
